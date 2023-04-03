package com.eliza.feignClient.controllers.exceptionsHandler;

import br.com.caelum.stella.validation.InvalidStateException;
import com.eliza.feignClient.services.exceptions.TaxaJurosNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class ControllerExceptionHandler {

    //erro de NOT FOUND
    public ResponseEntity<StandardError> taxaJurosNotFoundException(
            TaxaJurosNotFoundException tx,
            HttpServletRequest request
    ) {
        var error = StandardError.builder()
                .timestamp(now())
                .code(NOT_FOUND.value())
                .error(NOT_FOUND.getReasonPhrase())
                .message(tx.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(NOT_FOUND).body(error);
    }

    //erro de ARGUMENTO INVÁLIDO
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> argumentoNotValid(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        ValidationError vd = ValidationError.builder()
                .timestamp(now())
                .code(BAD_REQUEST.value())
                .error(BAD_REQUEST.getReasonPhrase())
                .message("Erro na validação dos campos!")
                .path(request.getRequestURI())
                .build();

        for(FieldError x : ex.getBindingResult().getFieldErrors()) {
            vd.addError(x.getField(), x.getDefaultMessage());
        }
        return ResponseEntity.status(BAD_REQUEST).body(vd);
    }

    //erro CNPJ num repetidos
    @ExceptionHandler(InvalidStateException.class)
    public ResponseEntity<StandardError> cnpjNotValid
            (InvalidStateException ex, HttpServletRequest request) {
        var error = StandardError.builder()
                .timestamp(now())
                .code(BAD_REQUEST.value())
                .error(BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(BAD_REQUEST).body(error);
    }
    
    //erro query TIPO DE CARACTER
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<StandardError> queryNotValid
    (MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        var error = StandardError.builder()
                .timestamp(now())
                .code(BAD_REQUEST.value())
                .error(BAD_REQUEST.getReasonPhrase())
                .message("campos aceitam apenas números")
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(BAD_REQUEST).body(error);
    }


    //erro query SIZE = 0
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StandardError> sizeNotValid
    (IllegalArgumentException ex, HttpServletRequest request) {
        var error = StandardError.builder()
                .timestamp(now())
                .code(BAD_REQUEST.value())
                .error(BAD_REQUEST.getReasonPhrase())
                .message("Tamanho da página tem que ser maior que 0")
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(BAD_REQUEST).body(error);
    }
}
