package com.fatec.biblioteca.factories;

import com.fatec.biblioteca.model.usuarios.Aluno;
import com.fatec.biblioteca.model.usuarios.Bibliotecario;
import com.fatec.biblioteca.model.usuarios.Professor;
import com.fatec.biblioteca.model.usuarios.Usuario;

/**
 * Factory para criar usuários.
 */
public final class UsuarioFactory {
    private UsuarioFactory() {}

    public static Usuario criarUsuario(String tipo, String nome) {
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo de usuário não pode ser nulo");
        }
        switch (tipo.toLowerCase()) {
            case "aluno":
                return new Aluno(nome);
            case "professor":
                return new Professor(nome);
            case "bibliotecario":
                return new Bibliotecario(nome);
            default:
                throw new IllegalArgumentException("Tipo de usuário inválido: " + tipo);
        }
    }
}
