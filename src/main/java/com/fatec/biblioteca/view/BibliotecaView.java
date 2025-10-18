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
                case "0" -> executar = false;
                default -> System.out.println("Opção inválida");
            }
        }
    }

    private void mostrarMenu() {
        System.out.println("\n--- Sistema da Biblioteca ---");
        System.out.println("1 - Cadastrar livro");
        System.out.println("2 - Listar livros");
        System.out.println("3 - Cadastrar usuário");
        System.out.println("4 - Listar usuários");
        System.out.println("5 - Registrar empréstimo");
        System.out.println("6 - Registrar devolução");
        System.out.println("7 - Listar empréstimos");
        System.out.println("8 - Gerar relatório completo");
        System.out.println("0 - Sair");
        System.out.print("Escolha: ");
    }

    private void cadastrarLivro() {
        System.out.print("Título: ");
        String titulo = scanner.nextLine();
        System.out.print("Autor: ");
        String autor = scanner.nextLine();
        Livro livro = LivroFactory.criarLivro(titulo, autor);
        controller.cadastrarLivro(livro);
        System.out.println("Livro cadastrado.");
    }

    private void listarLivros() {
        List<Livro> livros = controller.listarLivros();
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro cadastrado.");
            return;
        }
        livros.forEach(System.out::println);
    }

    private void cadastrarUsuario() {
        System.out.print("Tipo (aluno|professor|bibliotecario): ");
        String tipo = scanner.nextLine();
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        try {
            Usuario usuario = UsuarioFactory.criarUsuario(tipo, nome);
            controller.cadastrarUsuario(usuario);
            System.out.println("Usuário cadastrado.");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void listarUsuarios() {
        List<Usuario> usuarios = controller.listarUsuarios();
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
            return;
        }
        usuarios.forEach(System.out::println);
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

    private void listarEmprestimos() {
        List<Emprestimo> emprestimos = controller.listarEmprestimos();
        if (emprestimos.isEmpty()) {
            System.out.println("Nenhum empréstimo registrado.");
            return;
        }
        System.out.println("\nEmpréstimos:");
        emprestimos.forEach(System.out::println);
    }

    private void gerarRelatorio() {
        System.out.println("\n" + controller.gerarRelatorio());
    }
}
