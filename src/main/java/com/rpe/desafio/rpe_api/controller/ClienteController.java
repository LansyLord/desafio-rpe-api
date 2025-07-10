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
    public List<Cliente> listarTodos() {
        return clienteService.listarTodos();
    }

    @Operation(summary = "Cadastrar novo cliente")
    @PostMapping
    public Cliente salvar(@RequestBody Cliente cliente) {
        return clienteService.salvar(cliente);
    }

    @Operation(summary = "Buscar cliente por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
        return clienteService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Atualizar cliente existente")
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable Long id, @RequestBody Cliente cliente) {
        try {
            Cliente atualizado = clienteService.atualizar(id, cliente);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Listar clientes bloqueados")
    @GetMapping("/bloqueados")
    public List<Cliente> listarBloqueados() {
        return clienteService.listarBloqueados();
    }
}

