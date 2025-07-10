package com.rpe.desafio.rpe_api.repository;


import com.rpe.desafio.rpe_api.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findByStatusBloqueio(Cliente.StatusBloqueio status);

    @Query("SELECT c FROM Cliente c JOIN c.faturas f WHERE f.status = 'A' AND f.dataVencimento < CURRENT_DATE - 3 AND c.statusBloqueio = 'B'")
    List<Cliente> findClientesBloqueadosComFaturasAtrasadas();

}