package com.example.springTest.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.example.springTest.model.user.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    //método que fica com toda a lógica de geração do token
    public String generateToken(User user){

        // Trecho de código que faz a geração do token
        try {
            var algoritmo = Algorithm.HMAC256("12345678");// parte aonde define a senha da sua api e a assinatura do token
            return JWT.create()
                    .withIssuer("API products")// método que identifica quem esta gerando o token
                    .withSubject(user.getUsername()) // método que permite incluir informações no token
                    .withExpiresAt(dataExpiracao())
                    .sign(algoritmo);
        } catch (JWTCreationException exception){
            throw new RuntimeException("erro ao gerrar token jwt", exception);
        }
    }

    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
