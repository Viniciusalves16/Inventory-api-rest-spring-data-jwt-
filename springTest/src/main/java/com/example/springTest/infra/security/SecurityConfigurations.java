package com.example.springTest.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static javax.management.Query.and;

@Configuration // Classe de configuração
@EnableWebSecurity // Mostra ao spring que vamos personalizar as configurações de segurança
public class SecurityConfigurations {

    //Ele avalia os privilégios e permissões do usuário em relação às regras de segurança configuradas e retorna uma resposta apropriada indicando se o acesso é concedido ou negado.
    @Bean
    public SecurityFilterChain securityFilter(HttpSecurity http) throws Exception {
        // Desabilita contra ataques de csrf pois o próprio token ja acaba sendo uma proteção
        return http.csrf().disable().sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).build();


    }

    // Método criado para criar um objeto do tipo Authentication Manager que será utilizado na classe controller
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();// dessa forma o spring sabe que na coluna senha do banco de dados, eu estou utilizando o hash de senha Bycript
    }
}
