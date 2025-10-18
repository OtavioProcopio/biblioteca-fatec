package com.fatec.biblioteca.factories;

import com.fatec.biblioteca.model.Livro;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LivroFactoryTest {

    @Test
    void criarLivroRetornaInstanciaComDados() {
        Livro l = LivroFactory.criarLivro("Titulo", "Autor");
        assertNotNull(l);
        assertEquals("Titulo", l.getTitulo());
        assertEquals("Autor", l.getAutor());
        assertEquals(1, l.getQuantidade());
        assertTrue(l.isDisponivel());
    }

    @Test
    void criarLivroComTituloVazio() {
        assertThrows(IllegalArgumentException.class, () -> {
            LivroFactory.criarLivro("", "Autor");
        }, "Deveria lançar exceção para título vazio");
    }

    @Test
    void criarLivroComAutorVazio() {
        assertThrows(IllegalArgumentException.class, () -> {
            LivroFactory.criarLivro("Titulo", "");
        }, "Deveria lançar exceção para autor vazio");
    }

    @Test
    void criarLivroComTituloEAutorVazios() {
        assertThrows(IllegalArgumentException.class, () -> {
            LivroFactory.criarLivro("", "");
        }, "Deveria lançar exceção para título e autor vazios");
    }

    @Test
    void criarLivroComTituloNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            LivroFactory.criarLivro(null, "Autor");
        }, "Deveria lançar exceção para título nulo");
    }

    @Test
    void criarLivroComAutorNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            LivroFactory.criarLivro("Titulo", null);
        }, "Deveria lançar exceção para autor nulo");
    }

    @Test
    void criarLivroComTituloEAutorNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            LivroFactory.criarLivro(null, null);
        }, "Deveria lançar exceção para título e autor nulos");
    }

    @Test
    void criarLivroComTituloLongo() {
        String tituloLongo = "Este é um título muito longo para testar se o sistema consegue lidar com títulos extensos sem problemas";
        Livro l = LivroFactory.criarLivro(tituloLongo, "Autor");
        assertNotNull(l);
        assertEquals(tituloLongo, l.getTitulo());
    }

    @Test
    void criarLivroComAutorLongo() {
        String autorLongo = "Nome Completo do Autor com Vários Sobrenomes e Títulos Acadêmicos Dr. PhD";
        Livro l = LivroFactory.criarLivro("Titulo", autorLongo);
        assertNotNull(l);
        assertEquals(autorLongo, l.getAutor());
    }

    @Test
    void criarLivroComCaracteresEspeciais() {
        Livro l = LivroFactory.criarLivro("Título: Açúcar & Café!", "José María Ñoño");
        assertNotNull(l);
        assertEquals("Título: Açúcar & Café!", l.getTitulo());
        assertEquals("José María Ñoño", l.getAutor());
    }

    @Test
    void criarMultiplosLivrosSaoIndependentes() {
        Livro l1 = LivroFactory.criarLivro("Livro 1", "Autor 1");
        Livro l2 = LivroFactory.criarLivro("Livro 2", "Autor 2");
        
        assertNotNull(l1);
        assertNotNull(l2);
        assertNotSame(l1, l2);
        assertEquals("Livro 1", l1.getTitulo());
        assertEquals("Livro 2", l2.getTitulo());
    }

    @Test
    void criarLivroSempreComQuantidadeInicial() {
        Livro l1 = LivroFactory.criarLivro("A", "B");
        Livro l2 = LivroFactory.criarLivro("C", "D");
        Livro l3 = LivroFactory.criarLivro("E", "F");
        
        assertEquals(1, l1.getQuantidade());
        assertEquals(1, l2.getQuantidade());
        assertEquals(1, l3.getQuantidade());
    }
}
