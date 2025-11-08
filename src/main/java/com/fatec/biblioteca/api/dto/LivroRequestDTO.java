package com.fatec.biblioteca.api.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for creating a new book.
 */
public class LivroRequestDTO {
    @NotBlank(message = "Título é obrigatório")
    private String titulo;
    
    @NotBlank(message = "Autor é obrigatório")
    private String autor;

    public LivroRequestDTO() {
    }

    public LivroRequestDTO(String titulo, String autor) {
        this.titulo = titulo;
        this.autor = autor;
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
}
