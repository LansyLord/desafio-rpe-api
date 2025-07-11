package com.rpe.desafio.rpe_api.controller;

import com.rpe.desafio.rpe_api.model.Fatura;
import com.rpe.desafio.rpe_api.service.FaturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/faturas")
@Tag(name = "Faturas", description = "Operações relacionadas às faturas")
public class FaturaController {

    private final FaturaService faturaService;

    public FaturaController(FaturaService faturaService) {
        this.faturaService = faturaService;
    }

    @Operation(summary = "Listar faturas de um cliente")
    @GetMapping("/{clienteId}")
    public List<Fatura> listarPorCliente(@PathVariable Long clienteId) {
        return faturaService.listarPorCliente(clienteId);
    }

    @Operation(summary = "Registrar pagamento de uma fatura")
    @PutMapping("/{id}/pagamento")
    public ResponseEntity<Fatura> registrarPagamento(@PathVariable Long id) {
        Fatura fatura = faturaService.registrarPagamento(id);
        return ResponseEntity.ok(fatura);
    }

    @Operation(summary = "Listar faturas em atraso")
    @GetMapping("/atrasadas")
    public List<Fatura> listarAtrasadas() {
        return faturaService.listarFaturasAtrasadas();
    }
}
