package com.fatec.biblioteca.api.dto;

import com.fatec.biblioteca.model.Livro;

/**
 * DTO for book response.
 */
public class LivroResponseDTO {
    private String titulo;
    private String autor;
    private int quantidade;
    private boolean disponivel;

    public LivroResponseDTO() {
    }

    public LivroResponseDTO(Livro livro) {
        this.titulo = livro.getTitulo();
        this.autor = livro.getAutor();
        this.quantidade = livro.getQuantidade();
        this.disponivel = livro.isDisponivel();
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }
}
