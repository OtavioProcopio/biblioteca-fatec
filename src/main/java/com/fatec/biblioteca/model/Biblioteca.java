package com.fatec.biblioteca.model;

import com.fatec.biblioteca.model.interfaces.BibliotecaRepository;
import com.fatec.biblioteca.model.usuarios.Usuario;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.fatec.biblioteca.model.Emprestimo;


/**
 * Implementação singleton do repositório da biblioteca.
 */
public class Biblioteca implements BibliotecaRepository {

    private static Biblioteca instancia;

    private final List<Livro> livros;
    private final List<Usuario> usuarios;
    private final List<Emprestimo> emprestimos;

    private Biblioteca() {
        this.livros = new ArrayList<>();
        this.usuarios = new ArrayList<>();
        this.emprestimos = new ArrayList<>();
    }

    /**
     * Retorna a instância singleton da Biblioteca.
     */
    public static synchronized Biblioteca getInstancia() {
        if (instancia == null) {
            instancia = new Biblioteca();
        }
        return instancia;
    }

    @Override
    public void adicionarLivro(Livro livro) {
        this.livros.add(livro);
    }

    @Override
    public void removerLivro(Livro livro) {
        this.livros.remove(livro);
    }

    @Override
    public List<Livro> listarLivros() {
        return Collections.unmodifiableList(livros);
    }

    @Override
    public void adicionarUsuario(Usuario usuario) {
        this.usuarios.add(usuario);
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return Collections.unmodifiableList(usuarios);
    }

    @Override
    public Emprestimo registrarEmprestimo(Usuario usuario, Livro livro) {
        if (!livro.isDisponivel()) {
            throw new IllegalStateException("Livro indisponível para empréstimo.");
        }
        livro.emprestar();
        Emprestimo e = new Emprestimo(usuario, livro);
        this.emprestimos.add(e);
        return e;
    }

    @Override
    public void registrarDevolucao(Emprestimo emprestimo) {
        if (emprestimo == null) return;
        emprestimo.marcarDevolvido();
        Livro l = emprestimo.getLivro();
        l.devolver();
    }

    @Override
    public List<Emprestimo> listarEmprestimos() {
        return Collections.unmodifiableList(emprestimos);
    }

    @Override
    public String generateReportString() {

        StringBuilder report = new StringBuilder();
        report.append("Relatório da Biblioteca\n");
        report.append("======================\n\n");

        report.append("Livros:\n");
        if (livros.isEmpty()) {
            report.append("Nenhum livro cadastrado.\n");
        } else {
            for (Livro livro : livros) {
                report.append(livro).append("\n");
            }
        }

        report.append("\nUsuários:\n");
        if (usuarios.isEmpty()) {
            report.append("Nenhum usuário cadastrado.\n");
        } else {
            for (Usuario usuario : usuarios) {
                report.append(usuario).append("\n");
                // listar livros emprestados para este usuário
                for (Emprestimo emp : emprestimos) {
                    if (emp.getUsuario().equals(usuario) && emp.getStatus() == Emprestimo.Status.EMPRESTADO) {
                        report.append("  - Emprestado: ").append(emp.getLivro().getTitulo()).append(" (em " ).append(emp.getDataEmprestimo()).append(")\n");
                    }
                }
            }
        }

        report.append("\nEmpréstimos:\n");
        if (emprestimos.isEmpty()) {
            report.append("Nenhum empréstimo registrado.\n");
        } else {
            for (Emprestimo emp : emprestimos) {
                report.append(emp).append("\n");
            }
        }

        return report.toString();
    }


        

}
