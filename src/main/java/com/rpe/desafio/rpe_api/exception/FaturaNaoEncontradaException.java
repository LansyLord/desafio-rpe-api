package com.rpe.desafio.rpe_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FaturaNaoEncontradaException extends RuntimeException {
    public FaturaNaoEncontradaException(Long id) {
        super("Fatura de ID "+ id + " não encontrada!");
    }
}
