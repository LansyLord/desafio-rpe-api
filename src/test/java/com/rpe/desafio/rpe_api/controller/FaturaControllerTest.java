package com.rpe.desafio.rpe_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rpe.desafio.rpe_api.model.Cliente;
import com.rpe.desafio.rpe_api.model.Fatura;
import com.rpe.desafio.rpe_api.service.FaturaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FaturaController.class)
@Import(FaturaControllerTest.TestConfig.class)
class FaturaControllerTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        public FaturaService faturaService() {
            return Mockito.mock(FaturaService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FaturaService faturaService;

    @Autowired
    private ObjectMapper objectMapper;

    private Fatura faturaAtrasada;
    private Fatura faturaAtrasada2;
    private Fatura faturaPaga;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Cliente Teste");

        faturaAtrasada = new Fatura();
        faturaAtrasada.setId(101L);
        faturaAtrasada.setCliente(cliente);
        faturaAtrasada.setValor(150.0);
        faturaAtrasada.setDataVencimento(LocalDate.now().minusDays(10));
        faturaAtrasada.setStatus(Fatura.Status.A);

        faturaAtrasada2 = new Fatura();
        faturaAtrasada2.setId(102L);
        faturaAtrasada2.setCliente(cliente);
        faturaAtrasada2.setValor(320.0);
        faturaAtrasada2.setDataVencimento(LocalDate.now().minusDays(15));
        faturaAtrasada2.setStatus(Fatura.Status.A);

        faturaPaga = new Fatura();
        faturaPaga.setId(103L);
        faturaPaga.setCliente(cliente);
        faturaPaga.setValor(200.0);
        faturaPaga.setDataVencimento(LocalDate.now().minusMonths(1));
        faturaPaga.setStatus(Fatura.Status.P);
        faturaPaga.setDataPagamento(LocalDate.now().minusMonths(1).plusDays(5));
    }

    @Test
    void listarPorCliente_deveRetornarListaDeFaturasEStatusOK() throws Exception {
        Long clienteId = 1L;
        List<Fatura> faturasDoCliente = List.of(faturaAtrasada, faturaAtrasada2,faturaPaga);
        when(faturaService.listarPorCliente(clienteId)).thenReturn(faturasDoCliente);

        mockMvc.perform(get("/faturas/{clienteId}", clienteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(3))
                .andExpect(jsonPath("$[0].id").value(101L))
                .andExpect(jsonPath("$[1].id").value(102L))
                .andExpect(jsonPath("$[2].id").value(103L));
    }

    @Test
    void registrarPagamento_deveRetornarFaturaPagaEStatusOK() throws Exception {
        Long faturaIdParaPagar = 101L;
        faturaAtrasada.setStatus(Fatura.Status.P);
        faturaAtrasada.setDataPagamento(LocalDate.now());

        when(faturaService.registrarPagamento(faturaIdParaPagar)).thenReturn(faturaAtrasada);

        mockMvc.perform(put("/faturas/{id}/pagamento", faturaIdParaPagar)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(faturaIdParaPagar))
                .andExpect(jsonPath("$.status").value("P"));
    }

    @Test
    void listarAtrasadas_deveRetornarListaDeFaturasAtrasadasEStatusOK() throws Exception {
        List<Fatura> faturasAtrasadas = List.of(faturaAtrasada, faturaAtrasada2);
        when(faturaService.listarFaturasAtrasadas()).thenReturn(faturasAtrasadas);
        mockMvc.perform(get("/faturas/atrasadas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(101L))
                .andExpect(jsonPath("$[0].status").value("A"))
                .andExpect(jsonPath("$[1].id").value(102L))
                .andExpect(jsonPath("$[1].status").value("A"));
    }
}