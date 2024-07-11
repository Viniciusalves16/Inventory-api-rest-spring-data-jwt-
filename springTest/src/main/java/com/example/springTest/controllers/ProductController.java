package com.example.springTest.controllers;

import com.example.springTest.dtos.ProductRecord;
import com.example.springTest.model.ProductModel;
import com.example.springTest.repository.ProductRepository;
import com.example.springTest.service.ProductService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;
import java.util.UUID;



@RestController
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @Autowired //todo:Injeção de dependencia para extrair regras de negocio para a classe service
    ProductService productService;

    @PostMapping("/products")
    @Transactional
    public ResponseEntity addProduct(@RequestBody @Valid ProductRecord productRecord, UriComponentsBuilder uriComponentsBuilder) {
        var produto = (new ProductModel(productRecord));
        //Apenas transfere tudo no record para a entity
        productRepository.save(produto);
        var uri = uriComponentsBuilder.path("/products/{id}").buildAndExpand(produto.getIdProduct()).toUri();
        //buildAndExpand método que permite aumentar minha url compondo ela com o id como parametro
        //.toUri() transforma em uma url tudo que vem antes
        return ResponseEntity.created(uri).body(new ProductModel(productRecord));
        //utilizo uma variavel do tipo var para armazenar o método uriComponentsBuilder, montando uma url do meu serviço
    }

    @GetMapping("/products")                                     //todo:Implementacao de paginacao
    public ResponseEntity<Page<ProductModel>> getAllProducts(@PageableDefault(size = 2, sort = {"value"}, direction = Sort.Direction.DESC) Pageable p) {
        Page<ProductModel> productsList = productRepository.findAll(p); // recupera todos os produtos
        return productService.getAllProducts(productsList); //todo: Extraindo regras de negocios para a classe service
    }


    @GetMapping("/products/{id}")
    public ResponseEntity<Object> findOneProduct(@PathVariable(value = "id") UUID id) {
        Optional<ProductModel> productO = productRepository.findById(id);
        //productService.searchOneProduct(productO); //todo: extraindo regra de negocio para a classe service
        return productService.searchOneProduct(productO);
    }


    @PutMapping("/products/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id,
                                                @RequestBody @Valid ProductRecord productRecord) {
        Optional<ProductModel> product = productRepository.findById(id);
        return productService.updateOneProduct(product, productRecord);
    }


    @DeleteMapping("/products/{id}")
    public ResponseEntity delectProduct(@PathVariable(value = "id") UUID id,
                                        @RequestBody @Valid ProductRecord productRecord) {
        Optional<ProductModel> product = productRepository.findById(id);
        ResponseEntity r = productService.verifyDelete(product); //todo: extraindo regra de negocio para a classe service
        return r;
    }


}
