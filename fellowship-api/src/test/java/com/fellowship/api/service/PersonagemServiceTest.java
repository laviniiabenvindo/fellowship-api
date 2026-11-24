package com.fellowship.api.service;

import com.fellowship.api.exception.PersonagemNaoEncontradoException;
import com.fellowship.api.exception.RegraDeNegocioException;
import com.fellowship.api.model.Personagem;
import com.fellowship.api.repository.PersonagemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Ativa o Mockito no JUnit 5: cria os mocks e injeta no Service antes de cada teste.
// Nenhum Spring e nenhum PostgreSQL sobem aqui: é um teste UNITÁRIO.
@ExtendWith(MockitoExtension.class)
class PersonagemServiceTest {

    // Dependência simulada: o Repository é falso, então o teste não toca no banco.
    @Mock
    private PersonagemRepository repository;

    // Objeto real sob teste: o Mockito injeta o mock acima no construtor dele.
    @InjectMocks
    private PersonagemService service;

    // ---------- CENÁRIOS DE SUCESSO ----------

    @Test
    void deveCadastrarPersonagemValido() {
        // Arrange: cria o cenário e ensina o mock a responder
        Personagem frodo = new Personagem("Frodo", "Hobbit", "Condado", 33, true);
        when(repository.existsByNomeIgnoreCase("Frodo")).thenReturn(false);
        when(repository.save(any(Personagem.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act: executa o método real do Service
        Personagem resultado = service.cadastrar(frodo);

        // Assert: confere o resultado...
        assertEquals("Frodo", resultado.getNome());
        assertTrue(resultado.isPortadorDoAnel());
        // ...e confere a INTERAÇÃO: o Service realmente chamou save() uma vez
        verify(repository, times(1)).save(frodo);
    }

    @Test
    void deveBuscarPersonagemExistentePorId() {
        // Arrange
        Personagem gandalf = new Personagem("Gandalf", "Maia", "Valinor", 2019, false);
        when(repository.findById(1L)).thenReturn(Optional.of(gandalf));

        // Act
        Personagem resultado = service.buscarPorId(1L);

        // Assert
        assertSame(gandalf, resultado);
        verify(repository).findById(1L);
    }

    @Test
    void deveAtualizarPersonagemExistente() {
        // Arrange
        Personagem existente = new Personagem("Aragorn", "Humano", "Gondor", 87, false);
        Personagem novosDados = new Personagem("Aragorn II", "Humano", "Gondor", 88, false);
        when(repository.findById(2L)).thenReturn(Optional.of(existente));
        when(repository.save(any(Personagem.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Personagem resultado = service.atualizar(2L, novosDados);

        // Assert
        assertEquals("Aragorn II", resultado.getNome());
        assertEquals(88, resultado.getIdade());
        verify(repository).save(existente);
    }

    @Test
    void deveExcluirPersonagemExistente() {
        // Arrange
        when(repository.existsById(3L)).thenReturn(true);

        // Act
        service.excluir(3L);

        // Assert: método void, então só dá para verificar a interação
        verify(repository).deleteById(3L);
    }

    // ---------- CENÁRIOS DE ERRO ----------

    @Test
    void deveLancarExcecaoAoBuscarPersonagemInexistente() {
        // Arrange: Optional vazio simula "não achou no banco"
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert: assertThrows executa o código e exige que a exceção ocorra
        PersonagemNaoEncontradoException ex = assertThrows(
                PersonagemNaoEncontradoException.class,
                () -> service.buscarPorId(99L));

        assertTrue(ex.getMessage().contains("99"));
    }

    @Test
    void deveRecusarCadastroComNomeDuplicado() {
        // Arrange
        Personagem outroFrodo = new Personagem("Frodo", "Hobbit", "Condado", 50, false);
        when(repository.existsByNomeIgnoreCase("Frodo")).thenReturn(true);

        // Act + Assert
        assertThrows(RegraDeNegocioException.class, () -> service.cadastrar(outroFrodo));

        // Garante que NADA foi salvo quando a regra falhou
        verify(repository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoAoExcluirPersonagemInexistente() {
        // Arrange
        when(repository.existsById(42L)).thenReturn(false);

        // Act + Assert
        assertThrows(PersonagemNaoEncontradoException.class, () -> service.excluir(42L));
        verify(repository, never()).deleteById(any());
    }
}
