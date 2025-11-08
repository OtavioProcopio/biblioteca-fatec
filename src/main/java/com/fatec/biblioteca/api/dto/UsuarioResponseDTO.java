package com.fatec.biblioteca.api.dto;

import com.fatec.biblioteca.model.usuarios.Usuario;

/**
 * DTO for user response.
 */
public class UsuarioResponseDTO {
    private String nome;
    private String tipo;

    public UsuarioResponseDTO() {
    }

    public UsuarioResponseDTO(Usuario usuario) {
        this.nome = usuario.getNome();
        this.tipo = usuario.getClass().getSimpleName();
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
