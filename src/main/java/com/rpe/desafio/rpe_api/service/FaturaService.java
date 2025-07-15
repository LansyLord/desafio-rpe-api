package com.rpe.desafio.rpe_api.service;


import com.rpe.desafio.rpe_api.dto.FaturaDTO;
import com.rpe.desafio.rpe_api.exception.FaturaJaPagaException;
import com.rpe.desafio.rpe_api.exception.FaturaNaoEncontradaException;
import com.rpe.desafio.rpe_api.model.Fatura;
import com.rpe.desafio.rpe_api.repository.FaturaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class FaturaService {

    private final FaturaRepository faturaRepository;
    private final ClienteService clienteService;
    private static final BigDecimal TAXA_JUROS_DIARIA = new BigDecimal("0.001");

    public FaturaService(FaturaRepository faturaRepository, ClienteService clienteService) {
        this.faturaRepository = faturaRepository;
        this.clienteService = clienteService;
    }

    public List<FaturaDTO> listarPorCliente(Long clienteId) {
        List<Fatura> faturas = faturaRepository.findByClienteId(clienteId);

        return faturas.stream()
                .map(this::converterParaDTO)
                .toList();
    }

    public Fatura buscarPorId(Long id){
        return faturaRepository.findById(id)
                .orElseThrow(() -> new FaturaNaoEncontradaException(id));
    }

    @Transactional
    public FaturaDTO registrarPagamento(Long faturaId) {
        Fatura fatura = buscarPorId(faturaId);

        if (fatura.getStatus().equals(Fatura.Status.P)) {
            throw new FaturaJaPagaException(fatura.getId());
        }

        fatura.setStatus(Fatura.Status.P);
        fatura.setDataPagamento(LocalDate.now());

        Fatura faturaPaga = faturaRepository.save(fatura);

        verificarDesbloqueioDeCliente(faturaPaga.getCliente().getId());

        return converterParaDTO(faturaPaga);
    }

    public List<FaturaDTO> listarFaturasAtrasadas() {
        List<Fatura> faturasAtrasadas = faturaRepository.findByStatus(Fatura.Status.A);
        return faturasAtrasadas.stream()
                .map(this::converterParaDTO)
                .toList();
    }

    public List<Fatura> listarFaturasVencidas() {
        LocalDate limite = LocalDate.now().minusDays(3);
        return faturaRepository.findVencidasNaoPagas(limite);
    }

    @Transactional
    public void processarFaturasVencidas() {
        List<Fatura> vencidas = listarFaturasVencidas();

        for (Fatura fatura : vencidas) {
            fatura.setStatus(Fatura.Status.A);
            clienteService.marcarComoBloqueado(fatura.getCliente().getId());
        }
    }

    public void verificarDesbloqueioDeCliente(Long clienteID){
        if(faturaRepository.countByClienteIdAndStatus(clienteID, Fatura.Status.A) == 0)
            clienteService.marcarComoDesbloqueado(clienteID);

    }

    private BigDecimal calcularJuros(Fatura fatura) {
        if (fatura.getStatus() != Fatura.Status.A && fatura.getStatus() != Fatura.Status.P) {
            return BigDecimal.ZERO;
        }

        LocalDate dataFinal;
        if (fatura.getStatus() == Fatura.Status.P) {
            if (fatura.getDataPagamento() == null) {
                return BigDecimal.ZERO;
            }
            dataFinal = fatura.getDataPagamento();
        } else {
            dataFinal = LocalDate.now();
        }

        long diasAtraso = ChronoUnit.DAYS.between(fatura.getDataVencimento(), dataFinal);

        if (diasAtraso <= 0) {
            return BigDecimal.ZERO;
        }

        return fatura.getValor()
                .multiply(TAXA_JUROS_DIARIA)
                .multiply(new BigDecimal(diasAtraso))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private FaturaDTO converterParaDTO(Fatura fatura) {
        BigDecimal juros = calcularJuros(fatura);

        return new FaturaDTO(
                fatura.getId(),
                fatura.getCliente().getId(),
                fatura.getValor(),
                juros,
                fatura.getDataVencimento(),
                fatura.getStatus(),
                fatura.getDataPagamento()
        );
    }
}