package com.rpe.desafio.rpe_api.controller;

import com.rpe.desafio.rpe_api.model.Fatura;
import com.rpe.desafio.rpe_api.service.FaturaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/faturas")
public class FaturaController {

    private final FaturaService faturaService;

    public FaturaController(FaturaService faturaService) {
        this.faturaService = faturaService;
    }

    @GetMapping("/{clienteId}")
    public List<Fatura> listarPorCliente(@PathVariable Long clienteId) {
        return faturaService.listarPorCliente(clienteId);
    }

    @PutMapping("/{id}/pagamento")
    public ResponseEntity<Fatura> registrarPagamento(@PathVariable Long id) {
        try {
            Fatura fatura = faturaService.registrarPagamento(id);
            return ResponseEntity.ok(fatura);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/atrasadas")
    public List<Fatura> listarAtrasadas() {
        return faturaService.listarFaturasAtrasadas();
    }

    // (Opcional) endpoint para processar bloqueios
    @PostMapping("/processar-bloqueios")
    public ResponseEntity<Void> processarBloqueios() {
        faturaService.processarBloqueios();
        return ResponseEntity.ok().build();
    }
}
