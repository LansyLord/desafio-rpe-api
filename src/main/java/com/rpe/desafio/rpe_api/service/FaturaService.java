package com.rpe.desafio.rpe_api.service;


import com.rpe.desafio.rpe_api.model.Cliente;
import com.rpe.desafio.rpe_api.model.Fatura;
import com.rpe.desafio.rpe_api.repository.ClienteRepository;
import com.rpe.desafio.rpe_api.repository.FaturaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class FaturaService {

    private final FaturaRepository faturaRepository;
    private final ClienteRepository clienteRepository;

    public FaturaService(FaturaRepository faturaRepository, ClienteRepository clienteRepository) {
        this.faturaRepository = faturaRepository;
        this.clienteRepository = clienteRepository;
    }

    public List<Fatura> listarPorCliente(Long clienteId) {
        return faturaRepository.findByClienteId(clienteId);
    }

    public Optional<Fatura> buscarPorId(Long id) {
        return faturaRepository.findById(id);
    }

    public Fatura registrarPagamento(Long faturaId) {
        Fatura fatura = faturaRepository.findById(faturaId)
                .orElseThrow(() -> new RuntimeException("Fatura não encontrada"));

        fatura.setDataPagamento(LocalDate.now());
        fatura.setStatus(Fatura.Status.P);

        Fatura atualizada = faturaRepository.save(fatura);

//        // Regra de negócio: atualizar cliente se necessário
//        Cliente cliente = fatura.getCliente();
//        if (cliente.getStatusBloqueio() == Cliente.StatusBloqueio.B) {
//            cliente.setStatusBloqueio(Cliente.StatusBloqueio.A);
//            cliente.setLimiteCredito(3000.0); // Defina o valor padrão se necessário
//            clienteRepository.save(cliente);
//        }

        return atualizada;
    }

    public List<Fatura> listarFaturasAtrasadas() {
        return faturaRepository.findByStatus(Fatura.Status.A);
    }

    public void processarBloqueios() {
        List<Fatura> atrasadas = listarFaturasAtrasadas();
        for (Fatura f : atrasadas) {
            Cliente cliente = f.getCliente();
            if (cliente.getStatusBloqueio() == Cliente.StatusBloqueio.A) {
                cliente.setStatusBloqueio(Cliente.StatusBloqueio.B);
                cliente.setLimiteCredito(0.0);
                clienteRepository.save(cliente);
            }
        }
    }
}