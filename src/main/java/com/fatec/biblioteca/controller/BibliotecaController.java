package com.fatec.biblioteca.controller;

import com.fatec.biblioteca.model.Emprestimo;
import com.fatec.biblioteca.model.Livro;
import com.fatec.biblioteca.model.interfaces.BibliotecaRepository;
import com.fatec.biblioteca.model.usuarios.Usuario;
import java.util.List;

/**
 * 
 * Controller da biblioteca.
 * Aplica o princípio da inversão de dependências (DIP).
 * Recebe uma instância de BibliotecaRepository via injeção de dependência.
 */
public class BibliotecaController {
    private final BibliotecaRepository repository;

    public BibliotecaController(BibliotecaRepository repository) {
        this.repository = repository;
    }

    public void cadastrarLivro(Livro livro) {
        repository.adicionarLivro(livro);
    }

    public void removerLivro(Livro livro) {
        repository.removerLivro(livro);
    }

    public List<Livro> listarLivros() {
        return repository.listarLivros();
    }

    public void cadastrarUsuario(Usuario usuario) {
        repository.adicionarUsuario(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return repository.listarUsuarios();
    }

    public Emprestimo registrarEmprestimo(Usuario usuario, Livro livro) {
        return repository.registrarEmprestimo(usuario, livro);
    }

    public void registrarDevolucao(Emprestimo emprestimo) {
        repository.registrarDevolucao(emprestimo);
    }

    public List<Emprestimo> listarEmprestimos() {
        return repository.listarEmprestimos();
    }

    public String gerarRelatorio() {
        return repository.generateReportString();
    }
    
}
