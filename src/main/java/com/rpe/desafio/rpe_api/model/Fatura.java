package com.rpe.desafio.rpe_api.model;

import jakarta.persistence.*;


import java.time.LocalDate;

@Entity
@Table(name = "fatura")
public class Fatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    private LocalDate dataVencimento;

    private LocalDate dataPagamento;

    private Double valor;

    @Enumerated(EnumType.STRING)
    private Status status = Status.B;

    public enum Status {
        P, // Paga
        A, // Atrasada
        B  // Aberta
    }

    public Fatura(Long id, Cliente cliente, LocalDate dataVencimento, LocalDate dataPagamento, Double valor, Status status) {
        this.id = id;
        this.cliente = cliente;
        this.dataVencimento = dataVencimento;
        this.dataPagamento = dataPagamento;
        this.valor = valor;
        this.status = status;
    }

    public Fatura() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
