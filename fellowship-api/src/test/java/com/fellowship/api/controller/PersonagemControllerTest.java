package com.fellowship.api.controller;

import com.fellowship.api.exception.PersonagemNaoEncontradoException;
import com.fellowship.api.model.Personagem;
import com.fellowship.api.service.PersonagemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Sobe só a camada web (Controller + JSON + validação). O Service é simulado.
@WebMvcTest(PersonagemController.class)
class PersonagemControllerTest {

    @Autowired
    private MockMvc mockMvc; // faz requisições HTTP "de mentira", sem abrir porta

    @MockBean
    private PersonagemService service;

    @Test
    void deveRetornar404QuandoPersonagemNaoExiste() throws Exception {
        when(service.buscarPorId(99L)).thenThrow(new PersonagemNaoEncontradoException(99L));

        mockMvc.perform(get("/personagens/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void deveRetornar201AoCadastrarPersonagemValido() throws Exception {
        Personagem salvo = new Personagem("Sam", "Hobbit", "Condado", 38, false);
        when(service.cadastrar(any(Personagem.class))).thenReturn(salvo);

        String json = """
                {"nome":"Sam","raca":"Hobbit","localOrigem":"Condado","idade":38,"portadorDoAnel":false}
                """;

        mockMvc.perform(post("/personagens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Sam"));
    }

    @Test
    void deveRetornar400QuandoNomeEstaEmBranco() throws Exception {
        String json = """
                {"nome":"","raca":"Hobbit","localOrigem":"Condado","idade":38,"portadorDoAnel":false}
                """;

        mockMvc.perform(post("/personagens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }
}
