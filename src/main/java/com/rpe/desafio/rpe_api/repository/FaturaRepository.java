package com.rpe.desafio.rpe_api.repository;


import com.rpe.desafio.rpe_api.model.Fatura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FaturaRepository extends JpaRepository<Fatura, Long> {

    List<Fatura> findByClienteId(Long clienteId);

    List<Fatura> findByStatus(Fatura.Status status);

}