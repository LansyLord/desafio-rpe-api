package com.rpe.desafio.rpe_api.service;
import com.rpe.desafio.rpe_api.exception.ClienteNaoEncontradoException;
import com.rpe.desafio.rpe_api.model.Cliente;
import com.rpe.desafio.rpe_api.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João da Silva");
        cliente.setCpf("12345678901");
        cliente.setDataNascimento(LocalDate.of(1990, 1, 1));
        cliente.setStatusBloqueio(Cliente.StatusBloqueio.A);
        cliente.setLimiteCredito(1000.0);
    }

    @Test
    void deveListarTodosOsClientes() {
        when(clienteRepository.findAll()).thenReturn(List.of(cliente));

        List<Cliente> clientes = clienteService.listarTodos();

        assertEquals(1, clientes.size());
        verify(clienteRepository).findAll();
    }

    @Test
    void deveSalvarCliente() {
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        Cliente salvo = clienteService.salvar(cliente);

        assertNotNull(salvo);
        assertEquals("João da Silva", salvo.getNome());
        verify(clienteRepository).save(cliente);
    }

    @Test
    void deveBuscarClientePorId() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Cliente encontrado = clienteService.buscarPorId(1L);

        assertEquals(1L, encontrado.getId());
        assertEquals("João da Silva", encontrado.getNome());
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoForEncontrado() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ClienteNaoEncontradoException.class, () -> clienteService.buscarPorId(1L));
    }

    @Test
    void deveAtualizarCliente() {
        Cliente atualizado = new Cliente();
        atualizado.setNome("Joana Silva");
        atualizado.setCpf("09876543210");
        atualizado.setDataNascimento(LocalDate.of(1992, 2, 2));
        atualizado.setStatusBloqueio(Cliente.StatusBloqueio.B);
        atualizado.setLimiteCredito(0.0);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Cliente resultado = clienteService.atualizar(1L, atualizado);

        assertEquals("Joana Silva", resultado.getNome());
        assertEquals("09876543210", resultado.getCpf());
        assertEquals(Cliente.StatusBloqueio.B, resultado.getStatusBloqueio());
        assertEquals(0.0, resultado.getLimiteCredito());
        verify(clienteRepository).save(any());
    }

    @Test
    void deveListarClientesBloqueados() {
        when(clienteRepository.findByStatusBloqueio(Cliente.StatusBloqueio.B)).thenReturn(List.of(cliente));

        List<Cliente> bloqueados = clienteService.listarBloqueados();

        assertEquals(1, bloqueados.size());
        verify(clienteRepository).findByStatusBloqueio(Cliente.StatusBloqueio.B);
    }

    @Test
    void deveMarcarClienteComoBloqueadoEZerarCredito() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        clienteService.marcarComoBloqueado(1L);

        assertEquals(Cliente.StatusBloqueio.B, cliente.getStatusBloqueio());
        assertEquals(0.0, cliente.getLimiteCredito());
        verify(clienteRepository).save(cliente);
    }
}
