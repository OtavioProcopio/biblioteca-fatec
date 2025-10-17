package com.fatec.biblioteca.factories;

import com.fatec.biblioteca.model.usuarios.Aluno;
import com.fatec.biblioteca.model.usuarios.Bibliotecario;
import com.fatec.biblioteca.model.usuarios.Professor;
import com.fatec.biblioteca.model.usuarios.Usuario;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UsuarioFactoryTest {

    @Test
    void criarAluno() {
        Usuario u = UsuarioFactory.criarUsuario("aluno", "Joao");
        assertTrue(u instanceof Aluno);
        assertEquals("Joao", u.getNome());
    }

    @Test
    void criarProfessor() {
        Usuario u = UsuarioFactory.criarUsuario("professor", "Maria");
        assertTrue(u instanceof Professor);
        assertEquals("Maria", u.getNome());
    }

    @Test
    void criarBibliotecario() {
        Usuario u = UsuarioFactory.criarUsuario("bibliotecario", "Ana");
        assertTrue(u instanceof Bibliotecario);
        assertEquals("Ana", u.getNome());
    }

    @Test
    void tipoNuloLanca() {
        assertThrows(IllegalArgumentException.class, () -> UsuarioFactory.criarUsuario(null, "x"));
    }

    @Test
    void tipoInvalidoLanca() {
        assertThrows(IllegalArgumentException.class, () -> UsuarioFactory.criarUsuario("gestor", "x"));
    }
}
