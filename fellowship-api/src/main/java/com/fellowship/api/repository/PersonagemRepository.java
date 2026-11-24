package com.fellowship.api.repository;

import com.fellowship.api.model.Personagem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonagemRepository extends JpaRepository<Personagem, Long> {

    boolean existsByNomeIgnoreCase(String nome);
}
