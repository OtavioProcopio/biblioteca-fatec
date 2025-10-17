package com.fatec.biblioteca.model.usuarios;

/**
 * Usuário abstrato da biblioteca.
 */
public abstract class Usuario {
    protected String nome;

    public Usuario(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [nome=" + nome + "]";
    }
}
