package com.fatec.biblioteca.controller;

import com.fatec.biblioteca.model.Emprestimo;
import com.fatec.biblioteca.model.Livro;
import com.fatec.biblioteca.model.usuarios.Aluno;
import com.fatec.biblioteca.model.usuarios.Professor;
import com.fatec.biblioteca.model.usuarios.Bibliotecario;
import com.fatec.biblioteca.model.usuarios.Usuario;
import com.fatec.biblioteca.model.interfaces.BibliotecaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes do BibliotecaController")
class BibliotecaControllerTest {

    /**
     * Implementação fake do repositório para testes isolados.
     * Simula o comportamento do repositório sem dependências externas.
     */
    static class FakeRepository implements BibliotecaRepository {
        private final List<Livro> livros = new ArrayList<>();
        private final List<Usuario> usuarios = new ArrayList<>();
        private final List<Emprestimo> emprestimos = new ArrayList<>();

        @Override
        public void adicionarLivro(Livro livro) {
            livros.add(livro);
        }

        @Override
        public void removerLivro(Livro livro) {
            livros.remove(livro);
        }

        @Override
        public List<Livro> listarLivros() {
            return new ArrayList<>(livros);
        }

        @Override
        public void adicionarUsuario(Usuario usuario) {
            usuarios.add(usuario);
        }

        @Override
        public List<Usuario> listarUsuarios() {
            return new ArrayList<>(usuarios);
        }

        @Override
        public Emprestimo registrarEmprestimo(Usuario usuario, Livro livro) {
            if (!livro.isDisponivel()) {
                throw new IllegalStateException("Livro indisponível para empréstimo.");
            }
            livro.emprestar();
            Emprestimo emprestimo = new Emprestimo(usuario, livro);
            emprestimos.add(emprestimo);
            return emprestimo;
        }

        @Override
        public void registrarDevolucao(Emprestimo emprestimo) {
            if (emprestimo != null) {
                emprestimo.marcarDevolvido();
                emprestimo.getLivro().devolver();
            }
        }

        @Override
        public List<Emprestimo> listarEmprestimos() {
            return new ArrayList<>(emprestimos);
        }

        @Override
        public String generateReportString() {
            StringBuilder report = new StringBuilder();
            report.append("Relatório de Livros:\n");
            for (Livro livro : livros) {
                report.append("Título: ").append(livro.getTitulo())
                      .append(", Autor: ").append(livro.getAutor())
                      .append(", Quantidade: ").append(livro.getQuantidade())
                      .append(", Disponível: ").append(livro.isDisponivel() ? "Sim" : "Não")
                      .append("\n");
            }
            return report.toString();
        }
    }

    private FakeRepository repository;
    private BibliotecaController controller;

    @BeforeEach
    void setUp() {
        repository = new FakeRepository();
        controller = new BibliotecaController(repository);
    }

    @Nested
    @DisplayName("Testes de Gerenciamento de Livros")
    class GerenciamentoLivros {

        @Test
        @DisplayName("Deve cadastrar um livro com sucesso")
        void deveCadastrarLivro() {
            Livro livro = new Livro("Clean Code", "Robert Martin");
            
            controller.cadastrarLivro(livro);
            
            List<Livro> livros = controller.listarLivros();
            assertEquals(1, livros.size());
            assertEquals("Clean Code", livros.get(0).getTitulo());
        }

        @Test
        @DisplayName("Deve cadastrar múltiplos livros")
        void deveCadastrarMultiplosLivros() {
            Livro livro1 = new Livro("Livro 1", "Autor 1");
            Livro livro2 = new Livro("Livro 2", "Autor 2");
            Livro livro3 = new Livro("Livro 3", "Autor 3");
            
            controller.cadastrarLivro(livro1);
            controller.cadastrarLivro(livro2);
            controller.cadastrarLivro(livro3);
            
            List<Livro> livros = controller.listarLivros();
            assertEquals(3, livros.size());
        }

        @Test
        @DisplayName("Deve remover um livro com sucesso")
        void deveRemoverLivro() {
            Livro livro = new Livro("Teste", "Autor");
            controller.cadastrarLivro(livro);
            
            controller.removerLivro(livro);
            
            assertTrue(controller.listarLivros().isEmpty());
        }

        @Test
        @DisplayName("Deve listar livros vazio quando não há livros")
        void deveListarLivrosVazio() {
            List<Livro> livros = controller.listarLivros();
            
            assertNotNull(livros);
            assertTrue(livros.isEmpty());
        }

        @Test
        @DisplayName("Deve listar todos os livros cadastrados")
        void deveListarTodosLivros() {
            controller.cadastrarLivro(new Livro("A", "1"));
            controller.cadastrarLivro(new Livro("B", "2"));
            
            List<Livro> livros = controller.listarLivros();
            
            assertEquals(2, livros.size());
        }
    }

    @Nested
    @DisplayName("Testes de Gerenciamento de Usuários")
    class GerenciamentoUsuarios {

        @Test
        @DisplayName("Deve cadastrar um aluno")
        void deveCadastrarAluno() {
            Usuario aluno = new Aluno("João Silva");
            
            controller.cadastrarUsuario(aluno);
            
            List<Usuario> usuarios = controller.listarUsuarios();
            assertEquals(1, usuarios.size());
            assertEquals("João Silva", usuarios.get(0).getNome());
            assertTrue(usuarios.get(0) instanceof Aluno);
        }

        @Test
        @DisplayName("Deve cadastrar um professor")
        void deveCadastrarProfessor() {
            Usuario professor = new Professor("Maria Santos");
            
            controller.cadastrarUsuario(professor);
            
            List<Usuario> usuarios = controller.listarUsuarios();
            assertEquals(1, usuarios.size());
            assertTrue(usuarios.get(0) instanceof Professor);
        }

        @Test
        @DisplayName("Deve cadastrar um bibliotecário")
        void deveCadastrarBibliotecario() {
            Usuario bibliotecario = new Bibliotecario("Carlos Lima");
            
            controller.cadastrarUsuario(bibliotecario);
            
            List<Usuario> usuarios = controller.listarUsuarios();
            assertEquals(1, usuarios.size());
            assertTrue(usuarios.get(0) instanceof Bibliotecario);
        }

        @Test
        @DisplayName("Deve cadastrar múltiplos usuários de tipos diferentes")
        void deveCadastrarMultiplosUsuarios() {
            controller.cadastrarUsuario(new Aluno("Aluno1"));
            controller.cadastrarUsuario(new Professor("Professor1"));
            controller.cadastrarUsuario(new Bibliotecario("Bib1"));
            
            List<Usuario> usuarios = controller.listarUsuarios();
            assertEquals(3, usuarios.size());
        }

        @Test
        @DisplayName("Deve listar usuários vazio quando não há usuários")
        void deveListarUsuariosVazio() {
            List<Usuario> usuarios = controller.listarUsuarios();
            
            assertNotNull(usuarios);
            assertTrue(usuarios.isEmpty());
        }
    }

    @Nested
    @DisplayName("Testes de Empréstimos")
    class GerenciamentoEmprestimos {

        @Test
        @DisplayName("Deve registrar empréstimo com sucesso")
        void deveRegistrarEmprestimo() {
            Usuario usuario = new Aluno("Ana");
            Livro livro = new Livro("Clean Code", "Martin");
            controller.cadastrarUsuario(usuario);
            controller.cadastrarLivro(livro);
            
            Emprestimo emprestimo = controller.registrarEmprestimo(usuario, livro);
            
            assertNotNull(emprestimo);
            assertEquals(usuario, emprestimo.getUsuario());
            assertEquals(livro, emprestimo.getLivro());
            assertEquals(Emprestimo.Status.EMPRESTADO, emprestimo.getStatus());
        }

        @Test
        @DisplayName("Deve reduzir quantidade do livro ao registrar empréstimo")
        void deveReduzirQuantidadeLivroAoEmprestar() {
            Usuario usuario = new Aluno("Carlos");
            Livro livro = new Livro("Design Patterns", "GoF");
            controller.cadastrarUsuario(usuario);
            controller.cadastrarLivro(livro);
            
            assertEquals(1, livro.getQuantidade());
            
            controller.registrarEmprestimo(usuario, livro);
            
            assertEquals(0, livro.getQuantidade());
            assertFalse(livro.isDisponivel());
        }

        @Test
        @DisplayName("Deve lançar exceção ao emprestar livro indisponível")
        void deveLancarExcecaoAoEmprestarLivroIndisponivel() {
            Usuario usuario1 = new Aluno("User1");
            Usuario usuario2 = new Aluno("User2");
            Livro livro = new Livro("Livro Único", "Autor");
            
            controller.cadastrarUsuario(usuario1);
            controller.cadastrarUsuario(usuario2);
            controller.cadastrarLivro(livro);
            
            controller.registrarEmprestimo(usuario1, livro);
            
            assertThrows(IllegalStateException.class, () -> {
                controller.registrarEmprestimo(usuario2, livro);
            });
        }

        @Test
        @DisplayName("Deve adicionar empréstimo à lista de empréstimos")
        void deveAdicionarEmprestimoALista() {
            Usuario usuario = new Aluno("José");
            Livro livro = new Livro("Refactoring", "Fowler");
            controller.cadastrarUsuario(usuario);
            controller.cadastrarLivro(livro);
            
            assertTrue(controller.listarEmprestimos().isEmpty());
            
            controller.registrarEmprestimo(usuario, livro);
            
            assertEquals(1, controller.listarEmprestimos().size());
        }

        @Test
        @DisplayName("Deve registrar múltiplos empréstimos")
        void deveRegistrarMultiplosEmprestimos() {
            Usuario usuario1 = new Aluno("User1");
            Usuario usuario2 = new Aluno("User2");
            Livro livro1 = new Livro("Livro1", "Autor1");
            Livro livro2 = new Livro("Livro2", "Autor2");
            
            controller.cadastrarUsuario(usuario1);
            controller.cadastrarUsuario(usuario2);
            controller.cadastrarLivro(livro1);
            controller.cadastrarLivro(livro2);
            
            controller.registrarEmprestimo(usuario1, livro1);
            controller.registrarEmprestimo(usuario2, livro2);
            
            assertEquals(2, controller.listarEmprestimos().size());
        }
    }

    @Nested
    @DisplayName("Testes de Devoluções")
    class GerenciamentoDevolucoes {

        @Test
        @DisplayName("Deve registrar devolução e atualizar status")
        void deveRegistrarDevolucao() {
            Usuario usuario = new Aluno("Rita");
            Livro livro = new Livro("TDD", "Beck");
            controller.cadastrarUsuario(usuario);
            controller.cadastrarLivro(livro);
            
            Emprestimo emprestimo = controller.registrarEmprestimo(usuario, livro);
            assertEquals(Emprestimo.Status.EMPRESTADO, emprestimo.getStatus());
            
            controller.registrarDevolucao(emprestimo);
            
            assertEquals(Emprestimo.Status.DEVOLVIDO, emprestimo.getStatus());
            assertNotNull(emprestimo.getDataDevolucao());
        }

        @Test
        @DisplayName("Deve aumentar quantidade do livro ao devolver")
        void deveAumentarQuantidadeLivroAoDevolver() {
            Usuario usuario = new Aluno("Pedro");
            Livro livro = new Livro("Padrões", "Gamma");
            controller.cadastrarUsuario(usuario);
            controller.cadastrarLivro(livro);
            
            Emprestimo emprestimo = controller.registrarEmprestimo(usuario, livro);
            assertEquals(0, livro.getQuantidade());
            
            controller.registrarDevolucao(emprestimo);
            
            assertEquals(1, livro.getQuantidade());
            assertTrue(livro.isDisponivel());
        }

        @Test
        @DisplayName("Deve permitir novo empréstimo após devolução")
        void devePermitirNovoEmprestimoAposDevolucao() {
            Usuario usuario1 = new Aluno("User1");
            Usuario usuario2 = new Aluno("User2");
            Livro livro = new Livro("Livro", "Autor");
            
            controller.cadastrarUsuario(usuario1);
            controller.cadastrarUsuario(usuario2);
            controller.cadastrarLivro(livro);
            
            Emprestimo emprestimo1 = controller.registrarEmprestimo(usuario1, livro);
            controller.registrarDevolucao(emprestimo1);
            
            assertDoesNotThrow(() -> {
                controller.registrarEmprestimo(usuario2, livro);
            });
        }

        @Test
        @DisplayName("Não deve lançar exceção ao devolver null")
        void naoDeveLancarExcecaoAoDevolverNull() {
            assertDoesNotThrow(() -> {
                controller.registrarDevolucao(null);
            });
        }
    }

    @Nested
    @DisplayName("Testes de Relatórios")
    class GerenciamentoRelatorios {

        @Test
        @DisplayName("Deve gerar relatório com livros cadastrados")
        void deveGerarRelatorioComLivros() {
            Livro livro1 = new Livro("Livro1", "Autor1");
            Livro livro2 = new Livro("Livro2", "Autor2");
            
            controller.cadastrarLivro(livro1);
            controller.cadastrarLivro(livro2);
            
            String relatorio = controller.gerarRelatorio();
            
            assertNotNull(relatorio);
            assertTrue(relatorio.contains("Livro1"));
            assertTrue(relatorio.contains("Livro2"));
            assertTrue(relatorio.contains("Autor1"));
            assertTrue(relatorio.contains("Autor2"));
        }

        @Test
        @DisplayName("Deve gerar relatório com formato correto")
        void deveGerarRelatorioComFormatoCorreto() {
            Livro livro = new Livro("Clean Code", "Martin");
            controller.cadastrarLivro(livro);
            
            String relatorio = controller.gerarRelatorio();
            
            assertTrue(relatorio.contains("Título: Clean Code"));
            assertTrue(relatorio.contains("Autor: Martin"));
            assertTrue(relatorio.contains("Quantidade: 1"));
            assertTrue(relatorio.contains("Disponível: Sim"));
        }

        @Test
        @DisplayName("Deve mostrar livro indisponível no relatório")
        void deveMostrarLivroIndisponivelNoRelatorio() {
            Usuario usuario = new Aluno("User");
            Livro livro = new Livro("Livro", "Autor");
            
            controller.cadastrarUsuario(usuario);
            controller.cadastrarLivro(livro);
            controller.registrarEmprestimo(usuario, livro);
            
            String relatorio = controller.gerarRelatorio();
            
            assertTrue(relatorio.contains("Disponível: Não"));
            assertTrue(relatorio.contains("Quantidade: 0"));
        }

        @Test
        @DisplayName("Deve gerar relatório vazio quando não há livros")
        void deveGerarRelatorioVazio() {
            String relatorio = controller.gerarRelatorio();
            
            assertNotNull(relatorio);
            assertTrue(relatorio.contains("Relatório de Livros:"));
        }
    }

    @Nested
    @DisplayName("Testes de Integração")
    class TestesIntegracao {

        @Test
        @DisplayName("Deve executar fluxo completo de empréstimo e devolução")
        void deveExecutarFluxoCompleto() {
            // Arrange
            Usuario aluno = new Aluno("Maria");
            Livro livro = new Livro("Java Efetivo", "Bloch");
            
            // Act & Assert - Cadastro
            controller.cadastrarUsuario(aluno);
            controller.cadastrarLivro(livro);
            assertEquals(1, controller.listarUsuarios().size());
            assertEquals(1, controller.listarLivros().size());
            
            // Act & Assert - Empréstimo
            Emprestimo emprestimo = controller.registrarEmprestimo(aluno, livro);
            assertNotNull(emprestimo);
            assertEquals(0, livro.getQuantidade());
            assertEquals(1, controller.listarEmprestimos().size());
            
            // Act & Assert - Devolução
            controller.registrarDevolucao(emprestimo);
            assertEquals(1, livro.getQuantidade());
            assertEquals(Emprestimo.Status.DEVOLVIDO, emprestimo.getStatus());
        }

        @Test
        @DisplayName("Deve gerenciar múltiplos empréstimos simultâneos")
        void deveGerenciarMultiplosEmprestimosSimultaneos() {
            // 3 usuários, 3 livros
            Usuario u1 = new Aluno("User1");
            Usuario u2 = new Professor("User2");
            Usuario u3 = new Bibliotecario("User3");
            
            Livro l1 = new Livro("L1", "A1");
            Livro l2 = new Livro("L2", "A2");
            Livro l3 = new Livro("L3", "A3");
            
            controller.cadastrarUsuario(u1);
            controller.cadastrarUsuario(u2);
            controller.cadastrarUsuario(u3);
            controller.cadastrarLivro(l1);
            controller.cadastrarLivro(l2);
            controller.cadastrarLivro(l3);
            
            // Todos emprestam
            Emprestimo e1 = controller.registrarEmprestimo(u1, l1);
            Emprestimo e2 = controller.registrarEmprestimo(u2, l2);
            Emprestimo e3 = controller.registrarEmprestimo(u3, l3);
            
            assertEquals(3, controller.listarEmprestimos().size());
            
            // User1 devolve
            controller.registrarDevolucao(e1);
            assertEquals(Emprestimo.Status.DEVOLVIDO, e1.getStatus());
            assertEquals(Emprestimo.Status.EMPRESTADO, e2.getStatus());
            assertEquals(Emprestimo.Status.EMPRESTADO, e3.getStatus());
            
            // Todos devolvem
            controller.registrarDevolucao(e2);
            controller.registrarDevolucao(e3);
            
            assertTrue(l1.isDisponivel());
            assertTrue(l2.isDisponivel());
            assertTrue(l3.isDisponivel());
        }

        @Test
        @DisplayName("Deve manter consistência após remover livro")
        void deveManterConsistenciaAposRemoverLivro() {
            Livro l1 = new Livro("L1", "A1");
            Livro l2 = new Livro("L2", "A2");
            
            controller.cadastrarLivro(l1);
            controller.cadastrarLivro(l2);
            assertEquals(2, controller.listarLivros().size());
            
            controller.removerLivro(l1);
            assertEquals(1, controller.listarLivros().size());
            assertEquals("L2", controller.listarLivros().get(0).getTitulo());
        }
    }
}
