package com.fellowship.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "personagens")
public class Personagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "A raça é obrigatória")
    private String raca;

    @NotBlank(message = "O local de origem é obrigatório")
    private String localOrigem;

    @NotNull(message = "A idade é obrigatória")
    @Min(value = 0, message = "A idade não pode ser negativa")
    private Integer idade;

    private boolean portadorDoAnel;

    public Personagem() {
    }

    public Personagem(String nome, String raca, String localOrigem, Integer idade, boolean portadorDoAnel) {
        this.nome = nome;
        this.raca = raca;
        this.localOrigem = localOrigem;
        this.idade = idade;
        this.portadorDoAnel = portadorDoAnel;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getRaca() { return raca; }
    public void setRaca(String raca) { this.raca = raca; }

    public String getLocalOrigem() { return localOrigem; }
    public void setLocalOrigem(String localOrigem) { this.localOrigem = localOrigem; }

    public Integer getIdade() { return idade; }
    public void setIdade(Integer idade) { this.idade = idade; }

    public boolean isPortadorDoAnel() { return portadorDoAnel; }
    public void setPortadorDoAnel(boolean portadorDoAnel) { this.portadorDoAnel = portadorDoAnel; }
}
