package com.fatec.biblioteca.api.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for registering a loan.
 */
public class EmprestimoRequestDTO {
    @NotBlank(message = "Nome do usuário é obrigatório")
    private String nomeUsuario;
    
    @NotBlank(message = "Título do livro é obrigatório")
    private String tituloLivro;

    public EmprestimoRequestDTO() {
    }

    public EmprestimoRequestDTO(String nomeUsuario, String tituloLivro) {
        this.nomeUsuario = nomeUsuario;
        this.tituloLivro = tituloLivro;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getTituloLivro() {
        return tituloLivro;
    }

    public void setTituloLivro(String tituloLivro) {
        this.tituloLivro = tituloLivro;
    }
}
