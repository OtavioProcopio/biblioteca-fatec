package com.fatec.biblioteca.model;

/**
 * Entidade Livro.
 */
public class Livro {
    private final String titulo;
    private final String autor;
    private int quantidade;

    public Livro(String titulo, String autor) {
        this.titulo = titulo;
        this.autor = autor;
        this.quantidade = 1;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public boolean isDisponivel() {
        return quantidade > 0;
    }

    @Override
    public String toString() {
        return "Livro [titulo=" + titulo + ", autor=" + autor + ", quantidade=" + quantidade + "]";
    }

    /**
     * Realiza um empréstimo, reduzindo a quantidade disponível.
     */
    public void emprestar() {
        if (quantidade < 1) {
            throw new IllegalStateException("Livro indisponível para empréstimo.");
        }
        this.quantidade--;
    }

    /**
     * Devolve o livro aumentando a quantidade disponível.
     */
    public void devolver() {
        this.quantidade++;
    }
}
