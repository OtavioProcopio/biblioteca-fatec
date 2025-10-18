package com.fatec.biblioteca.view;

import com.fatec.biblioteca.controller.BibliotecaController;
import com.fatec.biblioteca.factories.LivroFactory;
import com.fatec.biblioteca.factories.UsuarioFactory;
import com.fatec.biblioteca.model.Emprestimo;
import com.fatec.biblioteca.model.Livro;
import com.fatec.biblioteca.model.usuarios.Usuario;
import java.util.List;
import java.util.Scanner;

/**
 * View de console da biblioteca. Fornece um menu simples.
 */
public class BibliotecaView {
    private final BibliotecaController controller;
    private final Scanner scanner = new Scanner(System.in);

    public BibliotecaView(BibliotecaController controller) {
        this.controller = controller;
    }

    public void iniciar() {
        boolean executar = true;
        while (executar) {
            mostrarMenu();
            String opcao = scanner.nextLine();
            switch (opcao) {
                case "1" -> cadastrarLivro();
                case "2" -> listarLivros();
                case "3" -> cadastrarUsuario();
                case "4" -> listarUsuarios();
                case "5" -> registrarEmprestimo();
                case "6" -> registrarDevolucao();
                case "7" -> listarEmprestimos();
                case "8" -> gerarRelatorio();
                case "0" -> {
                    executar = false;
                    System.out.println("Sistema encerrado. Obrigado!");
                }
                default -> System.out.println("Opção inválida! Tente novamente.");
            }
        }
    }

    /**
     * Exibe o menu principal do sistema.
     */
    public void mostrarMenu() {
        System.out.println("\n=== SISTEMA BIBLIOTECA ===");
        System.out.println("1. Cadastrar Livro");
        System.out.println("2. Listar Livros");
        System.out.println("3. Cadastrar Usuário");
        System.out.println("4. Listar Usuários");
        System.out.println("5. Registrar Empréstimo");
        System.out.println("6. Registrar Devolução");
        System.out.println("7. Listar Empréstimos");
        System.out.println("8. Gerar Relatório");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    /**
     * Cadastra um novo livro no sistema.
     */
    public void cadastrarLivro() {
        System.out.print("Digite o título do livro: ");
        String titulo = scanner.nextLine();
        System.out.print("Digite o autor do livro: ");
        String autor = scanner.nextLine();
        Livro livro = LivroFactory.criarLivro(titulo, autor);
        controller.cadastrarLivro(livro);
        System.out.println("Livro cadastrado com sucesso!");
    }

    /**
     * Lista todos os livros cadastrados.
     */
    public void listarLivros() {
        System.out.println("\n=== LIVROS CADASTRADOS ===");
        List<Livro> livros = controller.listarLivros();
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro cadastrado.");
        } else {
            for (Livro livro : livros) {
                System.out.println(livro);
            }
        }
    }

    /**
     * Cadastra um novo usuário no sistema.
     */
    public void cadastrarUsuario() {
        System.out.print("Digite o nome do usuário: ");
        String nome = scanner.nextLine();
        
        System.out.println("Escolha o tipo de usuário:");
        System.out.println("1. Aluno");
        System.out.println("2. Professor");
        System.out.println("3. Bibliotecário");
        System.out.print("Tipo: ");
        String tipoOpcao = scanner.nextLine();
        
        String tipo;
        switch (tipoOpcao) {
            case "1" -> tipo = "aluno";
            case "2" -> tipo = "professor";
            case "3" -> tipo = "bibliotecario";
            default -> {
                System.out.println("Opção inválida!");
                return;
            }
        }
        
        try {
            Usuario usuario = UsuarioFactory.criarUsuario(tipo, nome);
            controller.cadastrarUsuario(usuario);
            System.out.println("Usuário cadastrado com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    /**
     * Lista todos os usuários cadastrados.
     */
    public void listarUsuarios() {
        System.out.println("\n=== USUÁRIOS CADASTRADOS ===");
        List<Usuario> usuarios = controller.listarUsuarios();
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
        } else {
            for (Usuario usuario : usuarios) {
                System.out.println(usuario);
            }
        }
    }

    private void registrarEmprestimo() {
        System.out.println("\n--- Registrar Empréstimo ---");
        List<Usuario> usuarios = controller.listarUsuarios();
        List<Livro> livros = controller.listarLivros();
        
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado. Cadastre um usuário primeiro.");
            return;
        }
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro cadastrado. Cadastre um livro primeiro.");
            return;
        }
        
        System.out.println("Usuários disponíveis:");
        for (int i = 0; i < usuarios.size(); i++) {
            System.out.println(i + " - " + usuarios.get(i).getNome());
        }
        System.out.print("Escolha o índice do usuário: ");
        int idxUsuario = Integer.parseInt(scanner.nextLine());
        
        System.out.println("\nLivros disponíveis:");
        for (int i = 0; i < livros.size(); i++) {
            Livro l = livros.get(i);
            System.out.println(i + " - " + l.getTitulo() + " (disponível: " + l.isDisponivel() + ")");
        }
        System.out.print("Escolha o índice do livro: ");
        int idxLivro = Integer.parseInt(scanner.nextLine());
        
        try {
            Usuario u = usuarios.get(idxUsuario);
            Livro l = livros.get(idxLivro);
            controller.registrarEmprestimo(u, l);
            System.out.println("Empréstimo registrado com sucesso!");
        } catch (IllegalStateException e) {
            System.out.println("Erro: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro ao registrar empréstimo: " + e.getMessage());
        }
    }

    private void registrarDevolucao() {
        System.out.println("\n--- Registrar Devolução ---");
        List<Emprestimo> emprestimos = controller.listarEmprestimos().stream()
            .filter(e -> e.getStatus() == Emprestimo.Status.EMPRESTADO)
            .toList();
        
        if (emprestimos.isEmpty()) {
            System.out.println("Nenhum empréstimo ativo.");
            return;
        }
        
        System.out.println("Empréstimos ativos:");
        for (int i = 0; i < emprestimos.size(); i++) {
            Emprestimo e = emprestimos.get(i);
            System.out.println(i + " - " + e.getUsuario().getNome() + " | " + e.getLivro().getTitulo());
        }
        System.out.print("Escolha o índice do empréstimo para devolver: ");
        int idx = Integer.parseInt(scanner.nextLine());
        
        try {
            Emprestimo emp = emprestimos.get(idx);
            controller.registrarDevolucao(emp);
            System.out.println("Devolução registrada com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao registrar devolução: " + e.getMessage());
        }
    }

    /**
     * Lista todos os empréstimos registrados.
     */
    public void listarEmprestimos() {
        System.out.println("\n=== EMPRÉSTIMOS ===");
        List<Emprestimo> emprestimos = controller.listarEmprestimos();
        if (emprestimos.isEmpty()) {
            System.out.println("Nenhum empréstimo encontrado.");
        } else {
            for (Emprestimo emprestimo : emprestimos) {
                System.out.println(emprestimo);
            }
        }
    }

    /**
     * Gera e exibe o relatório completo da biblioteca.
     */
    public void gerarRelatorio() {
        String relatorio = controller.gerarRelatorio();
        System.out.println(relatorio);
    }
}
