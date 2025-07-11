package com.rpe.desafio.rpe_api;

import com.rpe.desafio.rpe_api.exception.FaturaJaPagaException;
import com.rpe.desafio.rpe_api.exception.FaturaNaoEncontradaException;
import com.rpe.desafio.rpe_api.model.Cliente;
import com.rpe.desafio.rpe_api.model.Fatura;
import com.rpe.desafio.rpe_api.repository.FaturaRepository;
import com.rpe.desafio.rpe_api.service.ClienteService;
import com.rpe.desafio.rpe_api.service.FaturaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FaturaServiceTest {

    @Mock
    private FaturaRepository faturaRepository;

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private FaturaService faturaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void buscarPorId_deveRetornarFatura_quandoExiste() {
        Fatura fatura = new Fatura();
        fatura.setId(1L);

        when(faturaRepository.findById(1L)).thenReturn(Optional.of(fatura));

        Fatura resultado = faturaService.buscarPorId(1L);

        assertEquals(1L, resultado.getId());
    }

    @Test
    void buscarPorId_deveLancarExcecao_quandoNaoExiste() {
        when(faturaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(FaturaNaoEncontradaException.class, () -> faturaService.buscarPorId(1L));
    }

    @Test
    void registrarPagamento_deveSalvarFaturaComStatusPago() {
        Fatura fatura = new Fatura();
        fatura.setId(1L);
        fatura.setStatus(Fatura.Status.B);

        when(faturaRepository.findById(1L)).thenReturn(Optional.of(fatura));
        when(faturaRepository.save(any(Fatura.class))).thenReturn(fatura);

        Fatura resultado = faturaService.registrarPagamento(1L);

        assertEquals(Fatura.Status.P, resultado.getStatus());
        assertNotNull(resultado.getDataPagamento());
    }

    @Test
    void registrarPagamento_deveLancarExcecao_quandoFaturaJaEstiverPaga() {
        Fatura fatura = new Fatura();
        fatura.setId(1L);
        fatura.setStatus(Fatura.Status.P);

        when(faturaRepository.findById(1L)).thenReturn(Optional.of(fatura));

        assertThrows(FaturaJaPagaException.class, () -> faturaService.registrarPagamento(1L));
    }

    @Test
    void marcarComoAtrasada_deveAtualizarStatus_quandoNaoEstiverAtrasada() {
        Fatura fatura = new Fatura();
        fatura.setId(1L);
        fatura.setStatus(Fatura.Status.B);

        when(faturaRepository.findById(1L)).thenReturn(Optional.of(fatura));

        faturaService.marcarComoAtrasada(1L);

        assertEquals(Fatura.Status.A, fatura.getStatus());
        verify(faturaRepository).save(fatura);
    }

    @Test
    void listarFaturasVencidas_deveChamarRepositoryComDataCorreta() {
        LocalDate expectedDate = LocalDate.now().minusDays(3);
        faturaService.listarFaturasVencidas();
        verify(faturaRepository).findVencidasNaoPagas(expectedDate);
    }

    @Test
    void processarFaturasVencidas_deveProcessarCadaFatura() {
        Fatura fatura = new Fatura();
        fatura.setId(1L);
        Cliente cliente = new Cliente();
        cliente.setId(10L);
        fatura.setCliente(cliente);

        when(faturaRepository.findVencidasNaoPagas(any())).thenReturn(List.of(fatura));
        when(faturaRepository.findById(1L)).thenReturn(Optional.of(fatura));

        faturaService.processarFaturasVencidas();

        verify(faturaRepository).save(fatura);
        verify(clienteService).marcarComoBloqueado(10L);
    }
}
