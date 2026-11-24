package com.fellowship.api.exception;

public class PersonagemNaoEncontradoException extends RuntimeException {

    public PersonagemNaoEncontradoException(Long id) {
        super("Personagem não encontrado com id " + id);
    }
}
