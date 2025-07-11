package com.rpe.desafio.rpe_api.repository;


import com.rpe.desafio.rpe_api.model.Fatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface FaturaRepository extends JpaRepository<Fatura, Long> {

    List<Fatura> findByClienteId(Long clienteId);

    List<Fatura> findByStatus(Fatura.Status status);

    @Query("SELECT f FROM Fatura f WHERE f.dataVencimento <= :limiteData AND f.status = 'B'")
    List<Fatura> findVencidasNaoPagas(@Param("limiteData") LocalDate limiteData);
}