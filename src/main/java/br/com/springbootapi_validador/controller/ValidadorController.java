package br.com.springbootapi_validador.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.springbootapi_validador.entity.Cliente;
import br.com.springbootapi_validador.entity.Token;
import br.com.springbootapi_validador.repository.ClienteRepository;
import br.com.springbootapi_validador.repository.TokenRepository;
import br.com.springbootapi_validador.utils.*;

@RestController
public class ValidadorController {

    @Autowired
    private TokenRepository _tokenRepository;
    private ClienteRepository _clienteRepository;
    private JWTToken _jwttoken;

    @RequestMapping(value = "/validador/{token}", method = RequestMethod.GET)
    public ResponseEntity<Token> GetById(@PathVariable(value = "token") String jwtToken) {
        Token token = _tokenRepository.findByToken(jwtToken);
        if (token == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            if (!_jwttoken.validateToken(jwtToken) && !token.getValidado()) {
                return new ResponseEntity<>(HttpStatus.REQUEST_TIMEOUT);
            } else {
                Optional<Cliente> oldCliente = _clienteRepository.findById(token.getClienteId());
                if (oldCliente.isPresent()) {
                    Cliente cliente = oldCliente.get();
                    cliente.setValido(true);
                    _clienteRepository.save(cliente);
                    token.setValidado(true);
                    _tokenRepository.save(token);
                    return new ResponseEntity<>(HttpStatus.OK);
                } else
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            }
        }
    }
}