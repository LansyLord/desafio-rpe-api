package com.rpe.desafio.rpe_api.service;

import com.rpe.desafio.rpe_api.dto.FaturaDTO;
import com.rpe.desafio.rpe_api.exception.FaturaJaPagaException;
import com.rpe.desafio.rpe_api.exception.FaturaNaoEncontradaException;
import com.rpe.desafio.rpe_api.model.Cliente;
import com.rpe.desafio.rpe_api.model.Fatura;
import com.rpe.desafio.rpe_api.repository.FaturaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FaturaServiceTest {

    @Mock
    private FaturaRepository faturaRepository;

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private FaturaService faturaService;

    private Fatura faturaAtrasada;
    private Fatura faturaEmAberto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Cliente cliente = new Cliente();
        cliente.setId(1L);

        faturaAtrasada = new Fatura();
        faturaAtrasada.setId(101L);
        faturaAtrasada.setCliente(cliente);
        faturaAtrasada.setValor(new BigDecimal("1000.00"));
        faturaAtrasada.setDataVencimento(LocalDate.now().minusDays(10));
        faturaAtrasada.setStatus(Fatura.Status.A);

        faturaEmAberto = new Fatura();
        faturaEmAberto.setId(102L);
        faturaEmAberto.setCliente(cliente);
        faturaEmAberto.setValor(new BigDecimal("500.00"));
        faturaEmAberto.setDataVencimento(LocalDate.now().plusDays(5));
        faturaEmAberto.setStatus(Fatura.Status.B);
    }

    @Test
    void buscarPorId_deveRetornarFatura_quandoExiste() {
        when(faturaRepository.findById(101L)).thenReturn(Optional.of(faturaAtrasada));
        Fatura resultado = faturaService.buscarPorId(101L);
        assertNotNull(resultado);
        assertEquals(101L, resultado.getId());
    }

    @Test
    void buscarPorId_deveLancarExcecao_quandoNaoExiste() {
        when(faturaRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(FaturaNaoEncontradaException.class, () -> faturaService.buscarPorId(1L));
    }

    @Test
    void registrarPagamento_DEVE_desbloquearCliente_quandoNaoHaOutrasFaturasAtrasadas() {
        when(faturaRepository.findById(101L)).thenReturn(Optional.of(faturaAtrasada));
        when(faturaRepository.save(any(Fatura.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(faturaRepository.countByClienteIdAndStatus(1L, Fatura.Status.A)).thenReturn(0);

        faturaService.registrarPagamento(101L);

        verify(clienteService, times(1)).marcarComoDesbloqueado(1L);
    }

    @Test
    void registrarPagamento_NAO_deveDesbloquearCliente_quandoExistemOutrasFaturasAtrasadas() {
        when(faturaRepository.findById(101L)).thenReturn(Optional.of(faturaAtrasada));
        when(faturaRepository.save(any(Fatura.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(faturaRepository.countByClienteIdAndStatus(1L, Fatura.Status.A)).thenReturn(1);

        faturaService.registrarPagamento(101L);

        verify(clienteService, never()).marcarComoDesbloqueado(anyLong());
    }

    @Test
    void registrarPagamento_deveLancarExcecao_quandoFaturaJaEstiverPaga() {
        Fatura faturaJaPaga = new Fatura();
        faturaJaPaga.setId(1L);
        faturaJaPaga.setStatus(Fatura.Status.P);
        when(faturaRepository.findById(1L)).thenReturn(Optional.of(faturaJaPaga));

        assertThrows(FaturaJaPagaException.class, () -> faturaService.registrarPagamento(1L));
    }

    @Test
    void processarFaturasVencidas_deveMarcarFaturaComoAtrasadaEBloquearCliente() {

        Fatura faturaParaAtrasar = new Fatura();
        faturaParaAtrasar.setId(102L);
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setStatusBloqueio(Cliente.StatusBloqueio.A);
        faturaParaAtrasar.setCliente(cliente);
        faturaParaAtrasar.setStatus(Fatura.Status.B);

        when(faturaRepository.findVencidasNaoPagas(any(LocalDate.class))).thenReturn(List.of(faturaParaAtrasar));

        faturaService.processarFaturasVencidas();

        assertEquals(Fatura.Status.A, faturaParaAtrasar.getStatus());
        verify(clienteService, times(1)).marcarComoBloqueado(1L);
    }
}