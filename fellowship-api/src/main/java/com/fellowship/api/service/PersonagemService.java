package com.fellowship.api.service;

import com.fellowship.api.exception.PersonagemNaoEncontradoException;
import com.fellowship.api.exception.RegraDeNegocioException;
import com.fellowship.api.model.Personagem;
import com.fellowship.api.repository.PersonagemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonagemService {

    private static final Logger log = LoggerFactory.getLogger(PersonagemService.class);

    private final PersonagemRepository repository;

    public PersonagemService(PersonagemRepository repository) {
        this.repository = repository;
    }

    public Personagem cadastrar(Personagem personagem) {
        if (repository.existsByNomeIgnoreCase(personagem.getNome())) {
            throw new RegraDeNegocioException("Já existe um personagem chamado " + personagem.getNome());
        }
        Personagem salvo = repository.save(personagem);
        log.info("Personagem cadastrado com id {}", salvo.getId());
        return salvo;
    }

    public List<Personagem> listar() {
        return repository.findAll();
    }

    public Personagem buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new PersonagemNaoEncontradoException(id));
    }

    public Personagem atualizar(Long id, Personagem dados) {
        Personagem existente = buscarPorId(id);
        existente.setNome(dados.getNome());
        existente.setRaca(dados.getRaca());
        existente.setLocalOrigem(dados.getLocalOrigem());
        existente.setIdade(dados.getIdade());
        existente.setPortadorDoAnel(dados.isPortadorDoAnel());
        log.info("Personagem {} atualizado", id);
        return repository.save(existente);
    }

    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new PersonagemNaoEncontradoException(id);
        }
        repository.deleteById(id);
        log.info("Personagem {} excluído", id);
    }
}
