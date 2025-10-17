package com.fatec.biblioteca.model.interfaces;

import com.fatec.biblioteca.model.Livro;
import com.fatec.biblioteca.model.usuarios.Usuario;
import com.fatec.biblioteca.model.Emprestimo;
import java.util.List;

/**
 * Contrato para repositório da biblioteca.
 */
public interface BibliotecaRepository {
    void adicionarLivro(Livro livro);
    void removerLivro(Livro livro);
    List<Livro> listarLivros();
    void adicionarUsuario(Usuario usuario);
    List<Usuario> listarUsuarios();
    String generateReportString();
    Emprestimo registrarEmprestimo(Usuario usuario, Livro livro);
    void registrarDevolucao(Emprestimo emprestimo);
    List<Emprestimo> listarEmprestimos();
}
