package com.fellowship.api.controller;

import com.fellowship.api.model.Personagem;
import com.fellowship.api.service.PersonagemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/personagens")
public class PersonagemController {

    private final PersonagemService service;

    public PersonagemController(PersonagemService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Personagem> cadastrar(@Valid @RequestBody Personagem personagem) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrar(personagem));
    }

    @GetMapping
    public List<Personagem> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Personagem buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Personagem atualizar(@PathVariable Long id, @Valid @RequestBody Personagem personagem) {
        return service.atualizar(id, personagem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
