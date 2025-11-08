package com.fatec.biblioteca.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for creating a new user.
 */
public class UsuarioRequestDTO {
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    
    @NotNull(message = "Tipo é obrigatório")
    private String tipo;

    public UsuarioRequestDTO() {
    }

    public UsuarioRequestDTO(String nome, String tipo) {
        this.nome = nome;
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
