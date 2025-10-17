package com.fatec.biblioteca.model;

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
        return "Usuario [nome=" + nome + "]";
    }

}
