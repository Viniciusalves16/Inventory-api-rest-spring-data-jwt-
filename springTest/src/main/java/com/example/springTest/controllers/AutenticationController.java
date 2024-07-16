package com.example.springTest.controllers;

import com.example.springTest.dtos.AuthenticationData;
import com.example.springTest.infra.security.DadosTokenJWT;
import com.example.springTest.infra.security.TokenService;
import com.example.springTest.model.user.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class AutenticationController {

    @Autowired// Método criado para na classe configuration para inicializar objeto
    private AuthenticationManager manager;//Classe do próprio spring que por baixo dos panos aciona o método de autenticação criado na service

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity logIn(@RequestBody @Valid AuthenticationData authenticationData) {//Cria-se um record para simular o envio do front
        var authenticationTokentoken = new UsernamePasswordAuthenticationToken(authenticationData.login(), authenticationData.senha());//Necessário conversão do objeto para atender o parametro de entrada do método manager.authenticate()
        var authentication = manager.authenticate(authenticationTokentoken);// Método responsável por realizar a autenticação

        // return ResponseEntity.ok().body("Usuário " + token.getName() + " autenticado com sucesso!");
        // método que chama o outro método criado no servico que gera o token
        // return ResponseEntity.ok().body(tokenService.generateToken((User) authentication.getPrincipal()));

        //padronizaçao da resposta do token
        var tokenJWt = tokenService.generateToken((User) authentication.getPrincipal());
        return ResponseEntity.ok().body(new DadosTokenJWT(tokenJWt));
    }

}
