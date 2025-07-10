package com.rpe.desafio.rpe_api.service;


import com.rpe.desafio.rpe_api.model.Cliente;
import com.rpe.desafio.rpe_api.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Cliente salvar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    public Cliente atualizar(Long id, Cliente clienteAtualizado) {
        return clienteRepository.findById(id).map(c -> {
            c.setNome(clienteAtualizado.getNome());
            c.setCpf(clienteAtualizado.getCpf());
            c.setDataNascimento(clienteAtualizado.getDataNascimento());
            c.setStatusBloqueio(clienteAtualizado.getStatusBloqueio());
            c.setLimiteCredito(clienteAtualizado.getLimiteCredito());
            return clienteRepository.save(c);
        }).orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }

    public List<Cliente> listarBloqueados() {
        return clienteRepository.findByStatusBloqueio(Cliente.StatusBloqueio.B);
    }

    public void atualizarLimiteClientesBloqueadosParaZero() {
        List<Cliente> bloqueados = listarBloqueados();
        for (Cliente c : bloqueados) {
            c.setLimiteCredito(0.0);
        }
        clienteRepository.saveAll(bloqueados);
    }

}
