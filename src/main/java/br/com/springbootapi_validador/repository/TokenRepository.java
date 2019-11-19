package br.com.springbootapi_validador.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import br.com.springbootapi_validador.entity.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query(value = "SELECT * FROM USERS WHERE token = ?1", nativeQuery = true)
    Token findByToken(String token);
}