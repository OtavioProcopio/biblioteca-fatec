package com.fatec.biblioteca.model;

import com.fatec.biblioteca.model.interfaces.BibliotecaRepository;
import com.fatec.biblioteca.model.usuarios.Usuario;
import com.fatec.biblioteca.model.usuarios.Aluno;
import com.fatec.biblioteca.model.usuarios.Professor;
import com.fatec.biblioteca.model.usuarios.Bibliotecario;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


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
        inicializarDadosPadrao();
    }

    /**
     * Inicializa a biblioteca com dados padrão de usuários e livros.
     */
    private void inicializarDadosPadrao() {
        // ==== 10 Usuários ====
        this.usuarios.add(new Aluno("Ana Souza"));
        this.usuarios.add(new Aluno("Bruno Lima"));
        this.usuarios.add(new Aluno("Carla Mendes"));
        this.usuarios.add(new Aluno("Diego Ferreira"));
        this.usuarios.add(new Aluno("Eduarda Alves"));
        this.usuarios.add(new Professor("Fernando Torres"));
        this.usuarios.add(new Professor("Gabriela Rocha"));
        this.usuarios.add(new Professor("Henrique Ramos"));
        this.usuarios.add(new Bibliotecario("Isabela Martins"));
        this.usuarios.add(new Bibliotecario("João Oliveira"));

        // ==== 10 Livros ====
        this.livros.add(new Livro("O Senhor dos Anéis", "J.R.R. Tolkien"));
        this.livros.add(new Livro("Dom Casmurro", "Machado de Assis"));
        this.livros.add(new Livro("Clean Code", "Robert C. Martin"));
        this.livros.add(new Livro("1984", "George Orwell"));
        this.livros.add(new Livro("O Pequeno Príncipe", "Antoine de Saint-Exupéry"));
        this.livros.add(new Livro("A Revolução dos Bichos", "George Orwell"));
        this.livros.add(new Livro("Harry Potter e a Pedra Filosofal", "J.K. Rowling"));
        this.livros.add(new Livro("Introdução à Programação em Java", "Deitel & Deitel"));
        this.livros.add(new Livro("A Arte da Guerra", "Sun Tzu"));
        this.livros.add(new Livro("O Hobbit", "J.R.R. Tolkien"));
    }

    /**
     * Retorna a instância singleton da Biblioteca.
     * Implementação thread-safe usando double-checked locking.
     */
    public static Biblioteca getInstancia() {
        if (instancia == null) {
            synchronized (Biblioteca.class) {
                if (instancia == null) {
                    instancia = new Biblioteca();
                }
            }
        }
        return instancia;
    }

    /**
     * Método para resetar a instância singleton - apenas para testes.
     * Deve ser usado com cuidado e somente em ambiente de teste.
     */
    public static synchronized void resetInstancia() {
        instancia = null;
    }

    /**
     * Adiciona um livro ao acervo da biblioteca.
     * @param livro O livro a ser adicionado (não pode ser nulo)
     * @throws IllegalArgumentException se o livro for nulo
     */
    @Override
    public void adicionarLivro(Livro livro) {
        if (livro == null) {
            throw new IllegalArgumentException("Livro não pode ser nulo");
        }
        this.livros.add(livro);
    }

    /**
     * Remove um livro do acervo da biblioteca.
     * @param livro O livro a ser removido (não pode ser nulo)
     * @throws IllegalArgumentException se o livro for nulo
     */
    @Override
    public void removerLivro(Livro livro) {
        if (livro == null) {
            throw new IllegalArgumentException("Livro não pode ser nulo");
        }
        this.livros.remove(livro);
    }

    /**
     * Retorna uma lista imutável de todos os livros cadastrados.
     * @return Lista imutável de livros
     */
    @Override
    public List<Livro> listarLivros() {
        return Collections.unmodifiableList(livros);
    }

    /**
     * Adiciona um usuário ao sistema da biblioteca.
     * @param usuario O usuário a ser adicionado (não pode ser nulo)
     * @throws IllegalArgumentException se o usuário for nulo
     */
    @Override
    public void adicionarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo");
        }
        this.usuarios.add(usuario);
    }

    /**
     * Retorna uma lista imutável de todos os usuários cadastrados.
     * @return Lista imutável de usuários
     */
    @Override
    public List<Usuario> listarUsuarios() {
        return Collections.unmodifiableList(usuarios);
    }

    /**
     * Registra um empréstimo de livro para um usuário.
     * @param usuario O usuário que está emprestando o livro (não pode ser nulo)
     * @param livro O livro a ser emprestado (não pode ser nulo e deve estar disponível)
     * @return O empréstimo criado
     * @throws IllegalArgumentException se usuário ou livro forem nulos
     * @throws IllegalStateException se o livro não estiver disponível
     */
    @Override
    public Emprestimo registrarEmprestimo(Usuario usuario, Livro livro) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo");
        }
        if (livro == null) {
            throw new IllegalArgumentException("Livro não pode ser nulo");
        }
        if (!livro.isDisponivel()) {
            throw new IllegalStateException("Livro indisponível para empréstimo.");
        }
        livro.emprestar();
        Emprestimo e = new Emprestimo(usuario, livro);
        this.emprestimos.add(e);
        return e;
    }

    /**
     * Registra a devolução de um livro emprestado.
     * @param emprestimo O empréstimo a ser finalizado (não pode ser nulo)
     * @throws IllegalArgumentException se o empréstimo for nulo
     * @throws IllegalStateException se o livro já foi devolvido
     */
    @Override
    public void registrarDevolucao(Emprestimo emprestimo) {
        if (emprestimo == null) {
            throw new IllegalArgumentException("Empréstimo não pode ser nulo");
        }
        if (emprestimo.getStatus() == Emprestimo.Status.DEVOLVIDO) {
            throw new IllegalStateException("Livro já foi devolvido anteriormente");
        }
        emprestimo.marcarDevolvido();
        Livro l = emprestimo.getLivro();
        l.devolver();
    }

    /**
     * Retorna uma lista imutável de todos os empréstimos registrados.
     * @return Lista imutável de empréstimos
     */
    @Override
    public List<Emprestimo> listarEmprestimos() {
        return Collections.unmodifiableList(emprestimos);
    }

    /**
     * Gera um relatório completo da biblioteca incluindo livros, usuários e empréstimos.
     * @return String contendo o relatório formatado
     */
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
