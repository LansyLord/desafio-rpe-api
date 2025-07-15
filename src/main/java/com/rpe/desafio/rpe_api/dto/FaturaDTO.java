package com.rpe.desafio.rpe_api.dto;

import com.rpe.desafio.rpe_api.model.Fatura;


import java.math.BigDecimal;
import java.time.LocalDate;

public record FaturaDTO(
        Long id,
        Long clienteId,
        BigDecimal valor,
        BigDecimal valorComJuros,
        LocalDate dataVencimento,
        Fatura.Status status,
        LocalDate dataPagamento
) {}
