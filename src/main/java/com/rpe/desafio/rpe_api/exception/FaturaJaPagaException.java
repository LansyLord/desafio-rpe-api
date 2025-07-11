package com.rpe.desafio.rpe_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class FaturaJaPagaException extends RuntimeException {
    public FaturaJaPagaException(Long id) {
        super("A fatura com ID " + id + " já está paga.");
    }
}
