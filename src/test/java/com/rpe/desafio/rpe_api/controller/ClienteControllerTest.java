package com.rpe.desafio.rpe_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rpe.desafio.rpe_api.model.Cliente;
import com.rpe.desafio.rpe_api.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
@Import(ClienteControllerTest.TestConfig.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ClienteService clienteService() {
            return Mockito.mock(ClienteService.class);
        }
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClienteService clienteService;

    private Cliente cliente1;
    private Cliente cliente2;

    @BeforeEach
    void setUp() {
        cliente1 = new Cliente();
        cliente1.setId(1L);
        cliente1.setNome("João da Silva");
        cliente1.setCpf("123.456.789-00");
        cliente1.setStatusBloqueio(Cliente.StatusBloqueio.A);
        cliente1.setDataNascimento(LocalDate.of(1990, 5, 15));

        cliente2 = new Cliente();
        cliente2.setId(2L);
        cliente2.setNome("Maria Oliveira");
        cliente2.setCpf("987.654.321-99");
        cliente2.setStatusBloqueio(Cliente.StatusBloqueio.B);
        cliente2.setDataNascimento(LocalDate.of(1985, 10, 20));
    }

    @Test
    void listarTodos_deveRetornarListaDeClientesEStatusOK() throws Exception {

        List<Cliente> clientes = Arrays.asList(cliente1, cliente2);
        when(clienteService.listarTodos()).thenReturn(clientes);

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].cpf").value("123.456.789-00"))
                .andExpect(jsonPath("$[1].cpf").value("987.654.321-99"));
    }

    @Test
    void salvar_deveRetornarClienteSalvoEStatusOK() throws Exception {
        when(clienteService.salvar(any(Cliente.class))).thenReturn(cliente1);

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("João da Silva"));
    }

    @Test
    void buscarPorId_deveRetornarClienteEStatusOK() throws Exception {
        when(clienteService.buscarPorId(1L)).thenReturn(cliente1);

        mockMvc.perform(get("/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("João da Silva"))
                .andExpect(jsonPath("$.cpf").value("123.456.789-00"));
    }

    @Test
    void atualizar_deveRetornarClienteAtualizadoEStatusOK() throws Exception {
        when(clienteService.atualizar(eq(1L), any(Cliente.class))).thenReturn(cliente1);

        mockMvc.perform(put("/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void listarBloqueados_deveRetornarListaDeClientesBloqueadosEStatusOK() throws Exception {
        List<Cliente> clientesBloqueados = List.of(cliente2);
        when(clienteService.listarBloqueados()).thenReturn(clientesBloqueados);

        mockMvc.perform(get("/clientes/bloqueados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].nome").value("Maria Oliveira"))
                .andExpect(jsonPath("$[0].cpf").value("987.654.321-99"))
                .andExpect(jsonPath("$[0].statusBloqueio").value("B"));
    }
}
