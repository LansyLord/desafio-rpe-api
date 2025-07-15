package com.rpe.desafio.rpe_api.service;


import com.rpe.desafio.rpe_api.exception.ClienteNaoEncontradoException;
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

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id).orElseThrow(() -> new ClienteNaoEncontradoException(id));
    }

    public Cliente atualizar(Long id, Cliente clienteAtualizado) {
        Cliente clienteExistente = buscarPorId(id);

        clienteExistente.setNome(clienteAtualizado.getNome());
        clienteExistente.setCpf(clienteAtualizado.getCpf());
        clienteExistente.setDataNascimento(clienteAtualizado.getDataNascimento());
        clienteExistente.setStatusBloqueio(clienteAtualizado.getStatusBloqueio());
        clienteExistente.setLimiteCredito(clienteAtualizado.getLimiteCredito());

        return clienteRepository.save(clienteExistente);
    }

    public List<Cliente> listarBloqueados() {
        return clienteRepository.findByStatusBloqueio(Cliente.StatusBloqueio.B);
    }

    public void marcarComoBloqueado(Long id){
        Cliente cliente = buscarPorId(id);
        if(cliente.getStatusBloqueio().equals(Cliente.StatusBloqueio.A))
            cliente.setStatusBloqueio(Cliente.StatusBloqueio.B);

        if(cliente.getLimiteCredito() >= 0)
            cliente.setLimiteCredito(0.0);

        clienteRepository.save(cliente);
    }

    public void marcarComoDesbloqueado(Long id){
        Cliente cliente = buscarPorId(id);
        if(cliente.getStatusBloqueio().equals(Cliente.StatusBloqueio.B))
            cliente.setStatusBloqueio(Cliente.StatusBloqueio.A);

        clienteRepository.save(cliente);
    }
}
