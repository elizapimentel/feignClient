package com.eliza.feignClient.services.exceptions;

//RuntimeException mais indicada para erros de dados inválidos
public class TaxaJurosNotFoundException extends RuntimeException {
    public TaxaJurosNotFoundException(String msg) {
        super(msg);
    }
}
