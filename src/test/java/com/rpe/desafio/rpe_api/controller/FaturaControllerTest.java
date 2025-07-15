package com.rpe.desafio.rpe_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rpe.desafio.rpe_api.dto.FaturaDTO; // Importe o DTO
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

import java.math.BigDecimal;
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

    private FaturaDTO faturaAtrasadaDTO1;
    private FaturaDTO faturaAtrasadaDTO2;
    private FaturaDTO faturaPagaDTO;
    private FaturaDTO faturaPagaRetornadaDTO;

    @BeforeEach
    void setUp() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Cliente Teste");


        faturaAtrasadaDTO1 = new FaturaDTO(101L, 1L, new BigDecimal("150.00"), new BigDecimal("151.50"),
                LocalDate.now().minusDays(10), Fatura.Status.A, null);

        faturaAtrasadaDTO2 = new FaturaDTO(102L, 1L, new BigDecimal("320.00"), new BigDecimal("324.80"),
                LocalDate.now().minusDays(15), Fatura.Status.A, null);

        faturaPagaDTO = new FaturaDTO(103L, 1L, new BigDecimal("200.00"), new BigDecimal("200.00"),
                LocalDate.now().minusMonths(1), Fatura.Status.P, LocalDate.now().minusMonths(1).plusDays(5));

        faturaPagaRetornadaDTO = new FaturaDTO(101L, 1L, new BigDecimal("150.00"), new BigDecimal("151.50"),
                LocalDate.now().minusDays(10), Fatura.Status.P, LocalDate.now());
    }

    @Test
    void listarPorCliente_deveRetornarListaDeFaturasEStatusOK() throws Exception {
        Long clienteId = 1L;

        List<FaturaDTO> faturasDtoDoCliente = List.of(faturaAtrasadaDTO1, faturaAtrasadaDTO2, faturaPagaDTO);
        when(faturaService.listarPorCliente(clienteId)).thenReturn(faturasDtoDoCliente);

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

        when(faturaService.registrarPagamento(faturaIdParaPagar)).thenReturn(faturaPagaRetornadaDTO);

        mockMvc.perform(put("/faturas/{id}/pagamento", faturaIdParaPagar)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(faturaIdParaPagar))
                .andExpect(jsonPath("$.status").value("P")); // Validação permanece a mesma
    }

    @Test
    void listarAtrasadas_deveRetornarListaDeFaturasAtrasadasEStatusOK() throws Exception {
        List<FaturaDTO> faturasAtrasadasDTO = List.of(faturaAtrasadaDTO1, faturaAtrasadaDTO2);
        when(faturaService.listarFaturasAtrasadas()).thenReturn(faturasAtrasadasDTO);

        mockMvc.perform(get("/faturas/atrasadas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(101L))
                .andExpect(jsonPath("$[0].status").value("A"))
                .andExpect(jsonPath("$[1].id").value(102L))
                .andExpect(jsonPath("$[1].status").value("A"));
    }
}