package com.rpe.desafio.rpe_api.service;


import com.rpe.desafio.rpe_api.exception.FaturaJaPagaException;
import com.rpe.desafio.rpe_api.exception.FaturaNaoEncontradaException;
import com.rpe.desafio.rpe_api.model.Cliente;
import com.rpe.desafio.rpe_api.model.Fatura;
import com.rpe.desafio.rpe_api.repository.ClienteRepository;
import com.rpe.desafio.rpe_api.repository.FaturaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class FaturaService {

    private final FaturaRepository faturaRepository;
    private final ClienteService clienteService;

    public FaturaService(FaturaRepository faturaRepository, ClienteService clienteService) {
        this.faturaRepository = faturaRepository;
        this.clienteService = clienteService;
    }

    public List<Fatura> listarPorCliente(Long clienteId) {
        return faturaRepository.findByClienteId(clienteId);
    }

    public Fatura buscarPorId(Long id){
        return faturaRepository.findById(id)
                .orElseThrow(() -> new FaturaNaoEncontradaException(id));
    }

    public void marcarComoAtrasada(Long id){
        Fatura fatura = buscarPorId(id);
        if(!fatura.getStatus().equals(Fatura.Status.A))
            fatura.setStatus(Fatura.Status.A);

        faturaRepository.save(fatura);
    }

    public Fatura registrarPagamento(Long faturaId) {
        Fatura fatura = buscarPorId(faturaId);

        if(fatura.getStatus().equals(Fatura.Status.P))
            throw new FaturaJaPagaException(fatura.getId());

        fatura.setDataPagamento(LocalDate.now());
        fatura.setStatus(Fatura.Status.P);

        return faturaRepository.save(fatura);
    }

    public List<Fatura> listarFaturasAtrasadas() {
        return faturaRepository.findByStatus(Fatura.Status.A);
    }

    public List<Fatura> listarFaturasVencidas() {
        LocalDate limite = LocalDate.now().minusDays(3);
        return faturaRepository.findVencidasNaoPagas(limite);
    }

    @Transactional
    public void processarFaturasVencidas() {
        List<Fatura> vencidas = listarFaturasVencidas();

        for (Fatura fatura : vencidas) {
            marcarComoAtrasada(fatura.getId());
            clienteService.marcarComoBloqueado(fatura.getCliente().getId());
        }
    }
}