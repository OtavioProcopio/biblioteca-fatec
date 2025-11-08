package com.fatec.biblioteca.api.dto;

import com.fatec.biblioteca.model.Emprestimo;

/**
 * DTO for loan response.
 */
public class EmprestimoResponseDTO {
    private String nomeUsuario;
    private String tituloLivro;
    private String dataEmprestimo;
    private String status;

    public EmprestimoResponseDTO() {
    }

    public EmprestimoResponseDTO(Emprestimo emprestimo) {
        this.nomeUsuario = emprestimo.getUsuario().getNome();
        this.tituloLivro = emprestimo.getLivro().getTitulo();
        this.dataEmprestimo = emprestimo.getDataEmprestimo().toString();
        this.status = emprestimo.getStatus().toString();
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

    public String getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(String dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
