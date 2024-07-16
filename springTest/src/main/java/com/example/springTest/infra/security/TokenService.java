package com.example.springTest.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.springTest.model.user.User;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}") // endereço da variavel de ambiente criada com a senha
    private String secret;

    //método que fica com toda a lógica de geração do token
    public String generateToken(User user) {


        // Trecho de código que faz a geração do token
        try {
            var algoritmo = Algorithm.HMAC256(secret);// parte aonde define a senha da sua api e a assinatura do token
            return JWT.create()
                    .withIssuer("API products")// método que identifica quem esta gerando o token
                    .withSubject(user.getUsername()) // método que permite incluir informações no token
                    .withExpiresAt(dataExpiracao())
                    .sign(algoritmo);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("erro ao gerrar token jwt", exception);
        }
    }

    // Método que verifica o toekn
    public String getSubject(String tokenJWT) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer( "API products")// ele verifica se o token possui esse objeto
                    .build()
                    .verify(tokenJWT)// verificaçao do token
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT inválido ou expirado!");
        }
    }

    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
