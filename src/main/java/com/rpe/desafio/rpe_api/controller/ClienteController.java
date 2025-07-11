package com.rpe.desafio.rpe_api.controller;


import com.rpe.desafio.rpe_api.model.Cliente;
import com.rpe.desafio.rpe_api.service.ClienteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@Tag(name = "Clientes", description = "Operações relacionadas aos clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Operation(summary = "Listar todos os clientes")
    @GetMapping
    public ResponseEntity<List<Cliente>> listarTodos() {
        return ResponseEntity.ok(clienteService.listarTodos());
    }

    @Operation(summary = "Cadastrar novo cliente")
    @PostMapping
    public ResponseEntity<Cliente> salvar(@RequestBody Cliente cliente) {
        return ResponseEntity.ok(clienteService.salvar(cliente));
    }

    @Operation(summary = "Buscar cliente por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @Operation(summary = "Atualizar cliente existente")
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable Long id, @RequestBody Cliente cliente) {
        return ResponseEntity.ok(clienteService.atualizar(id, cliente));
    }

    @Operation(summary = "Listar clientes bloqueados")
    @GetMapping("/bloqueados")
    public ResponseEntity<List<Cliente>> listarBloqueados() {
        return ResponseEntity.ok(clienteService.listarBloqueados());
    }
}

