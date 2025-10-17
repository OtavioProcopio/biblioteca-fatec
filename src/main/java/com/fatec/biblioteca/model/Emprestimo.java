package com.fatec.biblioteca.model;

import com.fatec.biblioteca.model.usuarios.Usuario;
import java.time.LocalDate;

/**
 * Entidade Emprestimo que representa o empréstimo de um livro por um usuário.
 */
public class Emprestimo {
    private final Usuario usuario;
    private final Livro livro;
    private final LocalDate dataEmprestimo;
    private LocalDate dataDevolucao;
    private Status status;

    public enum Status {
        EMPRESTADO, DEVOLVIDO
    }

    public Emprestimo(Usuario usuario, Livro livro) {
        this.usuario = usuario;
        this.livro = livro;
        this.dataEmprestimo = LocalDate.now();
        this.status = Status.EMPRESTADO;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Livro getLivro() {
        return livro;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public Status getStatus() {
        return status;
    }

    public void marcarDevolvido() {
        this.dataDevolucao = LocalDate.now();
        this.status = Status.DEVOLVIDO;
    }

    @Override
    public String toString() {
        return "Emprestimo[usuario=" + usuario.getNome() + ", livro=" + livro.getTitulo() + ", status=" + status + "]";
    }
}
