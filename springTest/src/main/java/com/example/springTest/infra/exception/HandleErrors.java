package com.example.springTest.infra.exception;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


// Anotação do spring que informa que a classe será responsável por tratar erros
@RestControllerAdvice
public class HandleErrors {

    // Anotação que mostra ao spring em qual situação o método será acionado
    // Dessa forma o spring sabe que em qualquer controller que a exceção for lançada ele chama esse método
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity HandleErrors404(EntityNotFoundException entityNotFoundException) {
        return new ResponseEntity(entityNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity HandleErrors400(MethodArgumentNotValidException ex) {//Exception que foi lançada
        var erros = ex.getFieldErrors();// método que retorna cada um dos erros que aconteceram
        return ResponseEntity.badRequest().body(erros.stream().map(DataErrors::new).toList());
    }

    private record DataErrors(String campo, String mensagem) {
        public DataErrors(FieldError erro){
            this(erro.getField(),erro.getDefaultMessage());
        }

    }
}
