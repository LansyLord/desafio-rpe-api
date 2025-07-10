package com.rpe.desafio.rpe_api.model;
import jakarta.persistence.*;


import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true)
    private String cpf;

    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    private StatusBloqueio statusBloqueio = StatusBloqueio.A;

    private Double limiteCredito;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Fatura> faturas;

    public enum StatusBloqueio {
        A, // Ativo
        B  // Bloqueado
    }

    public Cliente(Long id, String nome, String cpf, LocalDate dataNascimento, StatusBloqueio statusBloqueio, Double limiteCredito, List<Fatura> faturas) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.statusBloqueio = statusBloqueio;
        this.limiteCredito = limiteCredito;
        this.faturas = faturas;
    }

    public Cliente() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public StatusBloqueio getStatusBloqueio() {
        return statusBloqueio;
    }

    public void setStatusBloqueio(StatusBloqueio statusBloqueio) {
        this.statusBloqueio = statusBloqueio;
    }

    public Double getLimiteCredito() {
        return limiteCredito;
    }

    public void setLimiteCredito(Double limiteCredito) {
        this.limiteCredito = limiteCredito;
    }

    public List<Fatura> getFaturas() {
        return faturas;
    }

    public void setFaturas(List<Fatura> faturas) {
        this.faturas = faturas;
    }
}