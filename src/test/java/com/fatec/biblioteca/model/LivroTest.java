package com.fatec.biblioteca.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LivroTest {

    @Test
    void criarELerAtributos() {
        Livro livro = new Livro("O Alquimista", "Paulo Coelho");
        assertEquals("O Alquimista", livro.getTitulo());
        assertEquals("Paulo Coelho", livro.getAutor());
        assertEquals(1, livro.getQuantidade());
        assertTrue(livro.isDisponivel());
    }

    @Test
    void emprestarEDisponibilidade() {
        Livro livro = new Livro("Teste", "Autor");
        assertTrue(livro.isDisponivel());
        livro.emprestar();
        assertEquals(0, livro.getQuantidade());
        assertFalse(livro.isDisponivel());
    }

    @Test
    void emprestarQuandoIndisponivelDeveLancar() {
        Livro livro = new Livro("Teste", "Autor");
        livro.emprestar(); // quantidade passa para 0
        assertThrows(IllegalStateException.class, livro::emprestar);
    }

    @Test
    void devolverAumentaQuantidade() {
        Livro livro = new Livro("Teste", "Autor");
        livro.emprestar();
        assertEquals(0, livro.getQuantidade());
        livro.devolver();
        assertEquals(1, livro.getQuantidade());
    }
}
