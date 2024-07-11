package com.example.springTest.service;

import com.example.springTest.controllers.ProductController;
import com.example.springTest.dtos.ProductRecord;
import com.example.springTest.model.ProductModel;
import com.example.springTest.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    //todo: Regra de negocio Metodo getAllProducts
    public ResponseEntity getAllProducts(Page<ProductModel> productsList) {
        if (!productsList.isEmpty()) { // verifica se a lista de produtos não esta vazia
            for (ProductModel product : productsList) { // Percorre toda a lista
                UUID id = product.getIdProduct(); // seleciona o id do produto
                product.add(linkTo(methodOn(ProductController.class).findOneProduct(id)).withSelfRel()); // Monta o link acionando o metodo finOne
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(productsList);
    }

    public ResponseEntity searchOneProduct(Optional<ProductModel> productO) {
        if (productO.isEmpty()) {
            throw new EntityNotFoundException("Not Found");
        }
        productO.get().add(linkTo(methodOn(ProductController.class).getAllProducts(null)).withRel("Products List"));
        return ResponseEntity.status(HttpStatus.OK).body(productO);
    }
    //todo:Regra de negocio Metodo update
    public ResponseEntity updateOneProduct(Optional<ProductModel> product, ProductRecord productRecord){
        if (product.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not Found");
        }
        var productModel = product.get();
        BeanUtils.copyProperties(productRecord, productModel);
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(productModel));
    }


    //todo: Regra de negocio Metodo delete
    public ResponseEntity verifyDelete(Optional<ProductModel> productModel) {
        if (productModel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not Found");
        }
        productRepository.delete(productModel.get());
        return ResponseEntity.status(HttpStatus.OK).body("Product deleted sucessfully.");
    }


}
