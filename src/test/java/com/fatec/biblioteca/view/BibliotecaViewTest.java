package com.fatec.biblioteca.view;

import com.fatec.biblioteca.controller.BibliotecaController;
import com.fatec.biblioteca.model.Biblioteca;
import com.fatec.biblioteca.model.Livro;
import com.fatec.biblioteca.model.Emprestimo;
import com.fatec.biblioteca.model.usuarios.Usuario;
import com.fatec.biblioteca.model.usuarios.Aluno;
import com.fatec.biblioteca.model.usuarios.Professor;
import com.fatec.biblioteca.model.usuarios.Bibliotecario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Testes para a classe BibliotecaView.
 * Verifica a funcionalidade da interface de usuário e cobertura de 80%+.
 */
@DisplayName("BibliotecaView Tests")
class BibliotecaViewTest {

    private BibliotecaView view;
    private BibliotecaController controller;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        // Configura captura de saída do console
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        // Cria instâncias para teste
        Biblioteca biblioteca = Biblioteca.getInstancia();
        Biblioteca.resetInstancia(); // Limpa dados existentes
        biblioteca = Biblioteca.getInstancia(); // Nova instância limpa
        
        controller = new BibliotecaController(biblioteca);
        view = new BibliotecaView(controller);
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setIn(System.in);
    }

    // ===== TESTES DE LISTAGEM =====
    
    @Test
    @DisplayName("Deve listar livros corretamente")
    void deveListarLivros() {
        // When
        view.listarLivros();

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains("=== LIVROS CADASTRADOS ==="));
        
        // Verifica se os livros padrão estão sendo exibidos
        assertTrue(output.contains("O Senhor dos Anéis"));
        assertTrue(output.contains("J.R.R. Tolkien"));
        assertTrue(output.contains("Dom Casmurro"));
        assertTrue(output.contains("Machado de Assis"));
        assertTrue(output.contains("Clean Code"));
        assertTrue(output.contains("Robert C. Martin"));
    }

    @Test
    @DisplayName("Deve listar usuários corretamente")
    void deveListarUsuarios() {
        // When
        view.listarUsuarios();

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains("=== USUÁRIOS CADASTRADOS ==="));
        
        // Verifica se os usuários padrão estão sendo exibidos
        assertTrue(output.contains("Ana Souza"));
        assertTrue(output.contains("Bruno Lima"));
        assertTrue(output.contains("Fernando Torres"));
        assertTrue(output.contains("Isabela Martins"));
    }

    @Test
    @DisplayName("Deve listar empréstimos inicialmente vazio")
    void deveListarEmprestimosVazio() {
        // When
        view.listarEmprestimos();

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains("=== EMPRÉSTIMOS ==="));
        assertTrue(output.contains("Nenhum empréstimo encontrado"));
    }

    @Test
    @DisplayName("Deve gerar relatório da biblioteca")
    void deveGerarRelatorio() {
        // When
        view.gerarRelatorio();

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains("Relatório da Biblioteca"));
        assertTrue(output.contains("Livros:"));
        assertTrue(output.contains("Usuários:"));
        assertTrue(output.contains("Empréstimos:"));
        assertTrue(output.contains("O Senhor dos Anéis") || output.contains("Ana Souza"));
    }

    // ===== TESTES DE CADASTRO =====

    @Test
    @DisplayName("Deve processar cadastro de livro com entrada simulada")
    void deveCadastrarLivroComEntradaSimulada() {
        // Given
        String input = "Novo Livro Teste\nAutor Teste\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new BibliotecaView(controller);

        // When
        view.cadastrarLivro();

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains("Digite o título do livro:"));
        assertTrue(output.contains("Digite o autor do livro:"));
        assertTrue(output.contains("Livro cadastrado com sucesso!"));
        
        List<Livro> livros = controller.listarLivros();
        assertTrue(livros.stream().anyMatch(l -> l.getTitulo().equals("Novo Livro Teste")));
        assertEquals(11, livros.size());
    }

    @Test
    @DisplayName("Deve cadastrar usuário Aluno")
    void deveCadastrarUsuarioAluno() {
        // Given
        String input = "João Silva\n1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new BibliotecaView(controller);

        // When
        view.cadastrarUsuario();

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains("Digite o nome do usuário:"));
        assertTrue(output.contains("Escolha o tipo de usuário:"));
        assertTrue(output.contains("1. Aluno"));
        assertTrue(output.contains("Usuário cadastrado com sucesso!"));
        
        List<Usuario> usuarios = controller.listarUsuarios();
        assertTrue(usuarios.stream().anyMatch(u -> u.getNome().equals("João Silva") && u instanceof Aluno));
        assertEquals(11, usuarios.size());
    }

    @Test
    @DisplayName("Deve cadastrar usuário Professor")
    void deveCadastrarUsuarioProfessor() {
        // Given
        String input = "Dr. Carlos Pereira\n2\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new BibliotecaView(controller);

        // When
        view.cadastrarUsuario();

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains("2. Professor"));
        assertTrue(output.contains("Usuário cadastrado com sucesso!"));
        
        List<Usuario> usuarios = controller.listarUsuarios();
        assertTrue(usuarios.stream().anyMatch(u -> u.getNome().equals("Dr. Carlos Pereira") && u instanceof Professor));
    }

    @Test
    @DisplayName("Deve cadastrar usuário Bibliotecário")
    void deveCadastrarUsuarioBibliotecario() {
        // Given
        String input = "Maria Santos\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new BibliotecaView(controller);

        // When
        view.cadastrarUsuario();

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains("3. Bibliotecário"));
        assertTrue(output.contains("Usuário cadastrado com sucesso!"));
        
        List<Usuario> usuarios = controller.listarUsuarios();
        assertTrue(usuarios.stream().anyMatch(u -> u.getNome().equals("Maria Santos") && u instanceof Bibliotecario));
    }

    @Test
    @DisplayName("Deve tratar opção inválida no cadastro de usuário")
    void deveTratarOpcaoInvalidaUsuario() {
        // Given
        String input = "Nome Teste\n5\n"; // Opção inválida
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new BibliotecaView(controller);

        // When
        view.cadastrarUsuario();

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains("Opção inválida!"));
        
        // Verifica que o usuário não foi cadastrado
        List<Usuario> usuarios = controller.listarUsuarios();
        assertFalse(usuarios.stream().anyMatch(u -> u.getNome().equals("Nome Teste")));
        assertEquals(10, usuarios.size()); // Apenas os padrão
    }

    // ===== TESTES DE EMPRÉSTIMO E DEVOLUÇÃO (via reflection) =====

    @Test
    @DisplayName("Deve registrar empréstimo com sucesso usando reflexão")
    void deveRegistrarEmprestimoComSucessoReflection() throws Exception {
        // Given - simula escolha do usuário 0 e livro 0
        String input = "0\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new BibliotecaView(controller);

        // When - usa reflexão para chamar método privado
        Method metodo = BibliotecaView.class.getDeclaredMethod("registrarEmprestimo");
        metodo.setAccessible(true);
        metodo.invoke(view);

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains("--- Registrar Empréstimo ---"));
        assertTrue(output.contains("Usuários disponíveis:"));
        assertTrue(output.contains("Livros disponíveis:"));
        assertTrue(output.contains("Empréstimo registrado com sucesso!"));
        
        // Verifica se o empréstimo foi registrado
        List<Emprestimo> emprestimos = controller.listarEmprestimos();
        assertEquals(1, emprestimos.size());
    }

    @Test
    @DisplayName("Deve tratar erro ao registrar empréstimo de livro indisponível")
    void deveTratarErroLivroIndisponivelReflection() throws Exception {
        // Given - primeiro registra um empréstimo para tornar o livro indisponível
        List<Usuario> usuarios = controller.listarUsuarios();
        List<Livro> livros = controller.listarLivros();
        controller.registrarEmprestimo(usuarios.get(0), livros.get(0));
        
        // Tenta emprestar o mesmo livro novamente
        String input = "1\n0\n"; // Usuário 1, livro 0 (já emprestado)
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new BibliotecaView(controller);

        // When
        Method metodo = BibliotecaView.class.getDeclaredMethod("registrarEmprestimo");
        metodo.setAccessible(true);
        metodo.invoke(view);

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains("Erro:") || output.contains("indisponível"));
    }

    @Test
    @DisplayName("Deve tratar índices inválidos no empréstimo")
    void deveTratarIndicesInvalidosEmprestimoReflection() throws Exception {
        // Given - índices que não existem
        String input = "999\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new BibliotecaView(controller);

        // When
        Method metodo = BibliotecaView.class.getDeclaredMethod("registrarEmprestimo");
        metodo.setAccessible(true);
        metodo.invoke(view);

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains("Erro ao registrar empréstimo:"));
    }

    @Test
    @DisplayName("Deve registrar devolução com sucesso usando reflexão")
    void deveRegistrarDevolucaoComSucessoReflection() throws Exception {
        // Given - primeiro registra um empréstimo
        List<Usuario> usuarios = controller.listarUsuarios();
        List<Livro> livros = controller.listarLivros();
        controller.registrarEmprestimo(usuarios.get(0), livros.get(0));
        
        // Simula escolha do empréstimo 0 para devolução
        String input = "0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new BibliotecaView(controller);

        // When
        Method metodo = BibliotecaView.class.getDeclaredMethod("registrarDevolucao");
        metodo.setAccessible(true);
        metodo.invoke(view);

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains("--- Registrar Devolução ---"));
        assertTrue(output.contains("Empréstimos ativos:"));
        assertTrue(output.contains("Devolução registrada com sucesso!"));
        
        // Verifica se a devolução foi registrada
        List<Emprestimo> emprestimos = controller.listarEmprestimos();
        assertEquals(Emprestimo.Status.DEVOLVIDO, emprestimos.get(0).getStatus());
    }

    @Test
    @DisplayName("Deve mostrar mensagem quando não há empréstimos ativos")
    void deveMostrarMensagemSemEmprestimosAtivosReflection() throws Exception {
        // Given - nenhum empréstimo ativo
        view = new BibliotecaView(controller);

        // When
        Method metodo = BibliotecaView.class.getDeclaredMethod("registrarDevolucao");
        metodo.setAccessible(true);
        metodo.invoke(view);

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains("--- Registrar Devolução ---"));
        assertTrue(output.contains("Nenhum empréstimo ativo."));
    }

    @Test
    @DisplayName("Deve tratar índice inválido na devolução")
    void deveTratarIndiceInvalidoDevolucaoReflection() throws Exception {
        // Given - um empréstimo ativo, mas escolhe índice inválido
        List<Usuario> usuarios = controller.listarUsuarios();
        List<Livro> livros = controller.listarLivros();
        controller.registrarEmprestimo(usuarios.get(0), livros.get(0));
        
        String input = "999\n"; // Índice inválido
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new BibliotecaView(controller);

        // When
        Method metodo = BibliotecaView.class.getDeclaredMethod("registrarDevolucao");
        metodo.setAccessible(true);
        metodo.invoke(view);

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains("Erro ao registrar devolução:"));
    }

    // ===== TESTES DE VALIDAÇÃO E EDGE CASES =====

    @Test
    @DisplayName("Deve listar empréstimos após registrar alguns")
    void deveListarEmprestimosComDados() {
        // Given - registra alguns empréstimos
        List<Usuario> usuarios = controller.listarUsuarios();
        List<Livro> livros = controller.listarLivros();
        controller.registrarEmprestimo(usuarios.get(0), livros.get(0));
        controller.registrarEmprestimo(usuarios.get(1), livros.get(1));

        // When
        view.listarEmprestimos();

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains("=== EMPRÉSTIMOS ==="));
        assertFalse(output.contains("Nenhum empréstimo encontrado"));
        // Verifica se os empréstimos estão listados
        assertTrue(output.contains("Ana Souza") || output.contains("Bruno Lima"));
    }

    @Test
    @DisplayName("Deve mostrar menu principal corretamente")
    void deveMostrarMenuPrincipal() {
        // When
        view.mostrarMenu();

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains("=== SISTEMA BIBLIOTECA ==="));
        assertTrue(output.contains("1. Cadastrar Livro"));
        assertTrue(output.contains("2. Listar Livros"));
        assertTrue(output.contains("3. Cadastrar Usuário"));
        assertTrue(output.contains("4. Listar Usuários"));
        assertTrue(output.contains("5. Registrar Empréstimo"));
        assertTrue(output.contains("6. Registrar Devolução"));
        assertTrue(output.contains("7. Listar Empréstimos"));
        assertTrue(output.contains("8. Gerar Relatório"));
        assertTrue(output.contains("0. Sair"));
    }

    @Test
    @DisplayName("Deve validar que dados padrão foram carregados")
    void deveValidarDadosPadrao() {
        // When
        List<Livro> livros = controller.listarLivros();
        List<Usuario> usuarios = controller.listarUsuarios();

        // Then
        assertEquals(10, livros.size(), "Deveria ter 10 livros padrão");
        assertEquals(10, usuarios.size(), "Deveria ter 10 usuários padrão");
        
        // Verifica alguns livros específicos
        assertTrue(livros.stream().anyMatch(l -> l.getTitulo().equals("1984")));
        assertTrue(livros.stream().anyMatch(l -> l.getTitulo().equals("O Hobbit")));
        
        // Verifica alguns usuários específicos
        assertTrue(usuarios.stream().anyMatch(u -> u.getNome().equals("Ana Souza")));
        assertTrue(usuarios.stream().anyMatch(u -> u.getNome().equals("João Oliveira")));
    }

    @Test
    @DisplayName("Deve tratar entrada inválida no empréstimo - texto ao invés de número")
    void deveTratarEntradaInvalidaEmprestimoReflection() throws Exception {
        // Given - entrada não numérica
        String input = "abc\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new BibliotecaView(controller);

        // When
        Method metodo = BibliotecaView.class.getDeclaredMethod("registrarEmprestimo");
        metodo.setAccessible(true);
        
        // Then - deve capturar a exceção dentro de InvocationTargetException
        Exception exception = assertThrows(Exception.class, () -> metodo.invoke(view));
        assertTrue(exception.getCause() instanceof NumberFormatException);
    }

    @Test
    @DisplayName("Deve testar disponibilidade de livros na listagem de empréstimo")
    void deveTestarDisponibilidadeLivrosEmprestimoReflection() throws Exception {
        // Given - empresta um livro primeiro
        List<Usuario> usuarios = controller.listarUsuarios();
        List<Livro> livros = controller.listarLivros();
        controller.registrarEmprestimo(usuarios.get(0), livros.get(0));
        
        String input = "1\n1\n"; // Escolhe outro usuário e outro livro
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new BibliotecaView(controller);

        // When
        Method metodo = BibliotecaView.class.getDeclaredMethod("registrarEmprestimo");
        metodo.setAccessible(true);
        metodo.invoke(view);

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains("disponível: false") || output.contains("disponível: true"));
        assertTrue(output.contains("Empréstimo registrado com sucesso!"));
    }

    @Test
    @DisplayName("Deve validar empréstimo com entrada vazia")
    void deveValidarEmprestimoComEntradaVaziaReflection() throws Exception {
        // Given - entrada vazia/espaço
        String input = "\n\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new BibliotecaView(controller);

        // When
        Method metodo = BibliotecaView.class.getDeclaredMethod("registrarEmprestimo");
        metodo.setAccessible(true);
        
        // Then - deve capturar a exceção dentro de InvocationTargetException
        Exception exception = assertThrows(Exception.class, () -> metodo.invoke(view));
        assertTrue(exception.getCause() instanceof NumberFormatException);
    }

    @Test
    @DisplayName("Deve testar múltiplas devoluções")
    void deveTestarMultiplasDevolucoes() throws Exception {
        // Given - registra múltiplos empréstimos
        List<Usuario> usuarios = controller.listarUsuarios();
        List<Livro> livros = controller.listarLivros();
        controller.registrarEmprestimo(usuarios.get(0), livros.get(0));
        controller.registrarEmprestimo(usuarios.get(1), livros.get(1));
        controller.registrarEmprestimo(usuarios.get(2), livros.get(2));
        
        // When - testa devolução do segundo empréstimo
        String input = "1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new BibliotecaView(controller);
        
        Method metodo = BibliotecaView.class.getDeclaredMethod("registrarDevolucao");
        metodo.setAccessible(true);
        metodo.invoke(view);

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains("Devolução registrada com sucesso!"));
        
        // Verifica que ainda há empréstimos ativos
        List<Emprestimo> emprestimos = controller.listarEmprestimos();
        long ativos = emprestimos.stream()
                .filter(e -> e.getStatus() == Emprestimo.Status.EMPRESTADO)
                .count();
        assertEquals(2, ativos);
    }
}