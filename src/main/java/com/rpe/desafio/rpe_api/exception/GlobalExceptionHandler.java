package com.rpe.desafio.rpe_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(FaturaJaPagaException.class)
    public ResponseEntity<Map<String, String>> handleFaturaJaPaga(FaturaJaPagaException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("erro", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(ClienteNaoEncontradoException.class)
    public ResponseEntity<Map<String, String>> handleClienteNaoEncontrado(ClienteNaoEncontradoException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("erro", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(FaturaNaoEncontradaException.class)
    public ResponseEntity<Map<String, String>> handleFaturaNaoEncontrada(FaturaNaoEncontradaException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("erro", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}
