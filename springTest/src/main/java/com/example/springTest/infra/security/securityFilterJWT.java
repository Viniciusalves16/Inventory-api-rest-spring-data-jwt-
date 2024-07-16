package com.example.springTest.infra.security;

import com.example.springTest.repository.UserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Indica ao spring que pode iniciar a classe como sendo um componente
public class securityFilterJWT extends OncePerRequestFilter { // implementa uma interface filter

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    // Essa classe garante que o filtro realizado no token será executado uma unica vez por request
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var tokenJWT = recuperarToken(request);

        if (tokenJWT != null) {
            var subject = tokenService.getSubject(tokenJWT); // valida o token e recupera o subject
            var usuario = userRepository.findByLogin(subject);// recupera o objeto para autorizar

            var authentication = new UsernamePasswordAuthenticationToken(usuario,null,usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);// autenticaçao do usuário

            // System.out.println(subject);// recuperou o email extraindo do token
        }

        filterChain.doFilter(request, response); // Dessa forma ele encaminha o request e o response para o próximo filtro

    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }

        return null;
    }

}
