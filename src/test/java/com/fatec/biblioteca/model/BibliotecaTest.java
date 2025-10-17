package com.fatec.biblioteca.model;

import com.fatec.biblioteca.model.usuarios.Aluno;
import com.fatec.biblioteca.model.usuarios.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BibliotecaTest {

    @BeforeEach
    void resetSingleton() throws Exception {
        // Reflectively reset singleton between tests
        java.lang.reflect.Field f = Biblioteca.class.getDeclaredField("instancia");
        f.setAccessible(true);
        f.set(null, null);
    }

    @Test
    void getInstanciaRetornaMesmoObjeto() {
        Biblioteca b1 = Biblioteca.getInstancia();
        Biblioteca b2 = Biblioteca.getInstancia();
        assertSame(b1, b2);
    }

    @Test
    void adicionarEListarLivros() {
        Biblioteca b = Biblioteca.getInstancia();
        Livro l = new Livro("A", "B");
        b.adicionarLivro(l);
        List<Livro> livros = b.listarLivros();
        assertEquals(1, livros.size());
        assertEquals("A", livros.get(0).getTitulo());
    }

    @Test
    void removerLivro() {
        Biblioteca b = Biblioteca.getInstancia();
        Livro l = new Livro("A", "B");
        b.adicionarLivro(l);
        b.removerLivro(l);
        assertTrue(b.listarLivros().isEmpty());
    }

    @Test
    void adicionarEListarUsuarios() {
        Biblioteca b = Biblioteca.getInstancia();
        Usuario u = new Aluno("Pedro");
        b.adicionarUsuario(u);
        assertEquals(1, b.listarUsuarios().size());
        assertEquals("Pedro", b.listarUsuarios().get(0).getNome());
    }

    @Test
    void listasSaoImutaveis() {
        Biblioteca b = Biblioteca.getInstancia();
        b.adicionarLivro(new Livro("X","Y"));
        List<Livro> livros = b.listarLivros();
        assertThrows(UnsupportedOperationException.class, () -> livros.add(new Livro("Z","W")));
    }

    @Test
    void gerarRelatorioRetornaStringCorreta() {
        Biblioteca b = Biblioteca.getInstancia();
        Livro l1 = new Livro("Livro1", "Autor1");
        Livro l2 = new Livro("Livro2", "Autor2");
        b.adicionarLivro(l1);
        b.adicionarLivro(l2);   
        String relatorio = b.generateReportString();
        assertTrue(relatorio.contains("Livro1"));
        assertTrue(relatorio.contains("Livro2"));
        assertTrue(relatorio.contains("Autor1"));
        assertTrue(relatorio.contains("Autor2"));       
    }

    @Test
    void registrarEmprestimoComSucesso() {
        Biblioteca b = Biblioteca.getInstancia();
        Usuario u = new Aluno("José");
        Livro l = new Livro("Refactoring", "Fowler");
        b.adicionarUsuario(u);
        b.adicionarLivro(l);
        
        Emprestimo emp = b.registrarEmprestimo(u, l);
        
        assertNotNull(emp);
        assertEquals(u, emp.getUsuario());
        assertEquals(l, emp.getLivro());
        assertEquals(0, l.getQuantidade());
        assertEquals(1, b.listarEmprestimos().size());
    }

    @Test
    void registrarDevolucaoAtualizaEmprestimo() {
        Biblioteca b = Biblioteca.getInstancia();
        Usuario u = new Aluno("Rita");
        Livro l = new Livro("TDD", "Beck");
        b.adicionarUsuario(u);
        b.adicionarLivro(l);
        
        Emprestimo emp = b.registrarEmprestimo(u, l);
        assertEquals(Emprestimo.Status.EMPRESTADO, emp.getStatus());
        
        b.registrarDevolucao(emp);
        
        assertEquals(Emprestimo.Status.DEVOLVIDO, emp.getStatus());
        assertEquals(1, l.getQuantidade());
        assertNotNull(emp.getDataDevolucao());
    }

    @Test
    void relatorioIncluiEmprestimos() {
        Biblioteca b = Biblioteca.getInstancia();
        Usuario u = new Aluno("Luis");
        Livro l = new Livro("Código Limpo", "Martin");
        b.adicionarUsuario(u);
        b.adicionarLivro(l);
        
        b.registrarEmprestimo(u, l);
        
        String relatorio = b.generateReportString();
        assertTrue(relatorio.contains("Luis"));
        assertTrue(relatorio.contains("Código Limpo"));
        assertTrue(relatorio.contains("Empréstimos"));
        assertTrue(relatorio.contains("EMPRESTADO"));
    }

    @Test
    void emprestimoDeLivroIndisponivelLancaExcecao() {
        Biblioteca b = Biblioteca.getInstancia();
        Usuario u1 = new Aluno("User1");
        Usuario u2 = new Aluno("User2");
        Livro l = new Livro("Livro Raro", "Autor");
        b.adicionarUsuario(u1);
        b.adicionarUsuario(u2);
        b.adicionarLivro(l);
        
        b.registrarEmprestimo(u1, l);
        
        assertThrows(IllegalStateException.class, () -> {
            b.registrarEmprestimo(u2, l);
        });
    }
}
