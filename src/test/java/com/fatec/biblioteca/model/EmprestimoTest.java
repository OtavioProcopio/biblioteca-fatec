package com.fatec.biblioteca.model;

import com.fatec.biblioteca.model.usuarios.Aluno;
import com.fatec.biblioteca.model.usuarios.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class EmprestimoTest {

    private Usuario usuario;
    private Livro livro;

    @BeforeEach
    void setUp() {
        usuario = new Aluno("João Silva");
        livro = new Livro("Clean Code", "Robert Martin");
    }

    @Test
    void criarEmprestimoComSucesso() {
        Emprestimo emprestimo = new Emprestimo(usuario, livro);
        
        assertNotNull(emprestimo);
        assertEquals(usuario, emprestimo.getUsuario());
        assertEquals(livro, emprestimo.getLivro());
        assertNotNull(emprestimo.getDataEmprestimo());
        assertEquals(LocalDate.now(), emprestimo.getDataEmprestimo());
        assertEquals(Emprestimo.Status.EMPRESTADO, emprestimo.getStatus());
        assertNull(emprestimo.getDataDevolucao());
    }

    @Test
    void getUsuarioRetornaUsuarioCorreto() {
        Emprestimo emprestimo = new Emprestimo(usuario, livro);
        
        assertEquals(usuario, emprestimo.getUsuario());
        assertEquals("João Silva", emprestimo.getUsuario().getNome());
    }

    @Test
    void getLivroRetornaLivroCorreto() {
        Emprestimo emprestimo = new Emprestimo(usuario, livro);
        
        assertEquals(livro, emprestimo.getLivro());
        assertEquals("Clean Code", emprestimo.getLivro().getTitulo());
    }

    @Test
    void getDataEmprestimoRetornaDataAtual() {
        LocalDate dataAntes = LocalDate.now();
        Emprestimo emprestimo = new Emprestimo(usuario, livro);
        LocalDate dataDepois = LocalDate.now();
        
        assertNotNull(emprestimo.getDataEmprestimo());
        assertTrue(emprestimo.getDataEmprestimo().equals(dataAntes) || 
                   emprestimo.getDataEmprestimo().equals(dataDepois));
    }

    @Test
    void statusInicialDeveSerEmprestado() {
        Emprestimo emprestimo = new Emprestimo(usuario, livro);
        
        assertEquals(Emprestimo.Status.EMPRESTADO, emprestimo.getStatus());
    }

    @Test
    void dataDevolucaoInicialmenteNull() {
        Emprestimo emprestimo = new Emprestimo(usuario, livro);
        
        assertNull(emprestimo.getDataDevolucao());
    }

    @Test
    void marcarDevolvidoAtualizaStatus() {
        Emprestimo emprestimo = new Emprestimo(usuario, livro);
        assertEquals(Emprestimo.Status.EMPRESTADO, emprestimo.getStatus());
        
        emprestimo.marcarDevolvido();
        
        assertEquals(Emprestimo.Status.DEVOLVIDO, emprestimo.getStatus());
    }

    @Test
    void marcarDevolvidoDefineDataDevolucao() {
        Emprestimo emprestimo = new Emprestimo(usuario, livro);
        assertNull(emprestimo.getDataDevolucao());
        
        LocalDate dataAntes = LocalDate.now();
        emprestimo.marcarDevolvido();
        LocalDate dataDepois = LocalDate.now();
        
        assertNotNull(emprestimo.getDataDevolucao());
        assertTrue(emprestimo.getDataDevolucao().equals(dataAntes) || 
                   emprestimo.getDataDevolucao().equals(dataDepois));
    }

    @Test
    void marcarDevolvidoMultiplasVezesMantemStatusDevolvido() {
        Emprestimo emprestimo = new Emprestimo(usuario, livro);
        
        emprestimo.marcarDevolvido();
        emprestimo.marcarDevolvido();
        
        assertEquals(Emprestimo.Status.DEVOLVIDO, emprestimo.getStatus());
        assertNotNull(emprestimo.getDataDevolucao());
    }

    @Test
    void toStringContemInformacoesBasicas() {
        Emprestimo emprestimo = new Emprestimo(usuario, livro);
        
        String resultado = emprestimo.toString();
        
        assertNotNull(resultado);
        assertTrue(resultado.contains("João Silva"));
        assertTrue(resultado.contains("Clean Code"));
        assertTrue(resultado.contains("EMPRESTADO"));
    }

    @Test
    void toStringAposDevolver() {
        Emprestimo emprestimo = new Emprestimo(usuario, livro);
        emprestimo.marcarDevolvido();
        
        String resultado = emprestimo.toString();
        
        assertNotNull(resultado);
        assertTrue(resultado.contains("João Silva"));
        assertTrue(resultado.contains("Clean Code"));
        assertTrue(resultado.contains("DEVOLVIDO"));
    }

    @Test
    void statusEnumTemDoisValores() {
        Emprestimo.Status[] valores = Emprestimo.Status.values();
        
        assertEquals(2, valores.length);
        assertEquals(Emprestimo.Status.EMPRESTADO, valores[0]);
        assertEquals(Emprestimo.Status.DEVOLVIDO, valores[1]);
    }

    @Test
    void statusEnumValueOfFunciona() {
        assertEquals(Emprestimo.Status.EMPRESTADO, 
                     Emprestimo.Status.valueOf("EMPRESTADO"));
        assertEquals(Emprestimo.Status.DEVOLVIDO, 
                     Emprestimo.Status.valueOf("DEVOLVIDO"));
    }

    @Test
    void emprestimoComDiferentesUsuarios() {
        Usuario aluno = new Aluno("Maria");
        Usuario professor = new com.fatec.biblioteca.model.usuarios.Professor("Carlos");
        Usuario bibliotecario = new com.fatec.biblioteca.model.usuarios.Bibliotecario("Ana");
        
        Emprestimo emp1 = new Emprestimo(aluno, livro);
        Emprestimo emp2 = new Emprestimo(professor, livro);
        Emprestimo emp3 = new Emprestimo(bibliotecario, livro);
        
        assertEquals("Maria", emp1.getUsuario().getNome());
        assertEquals("Carlos", emp2.getUsuario().getNome());
        assertEquals("Ana", emp3.getUsuario().getNome());
    }

    @Test
    void emprestimoComDiferentesLivros() {
        Livro livro1 = new Livro("Livro 1", "Autor 1");
        Livro livro2 = new Livro("Livro 2", "Autor 2");
        
        Emprestimo emp1 = new Emprestimo(usuario, livro1);
        Emprestimo emp2 = new Emprestimo(usuario, livro2);
        
        assertEquals("Livro 1", emp1.getLivro().getTitulo());
        assertEquals("Livro 2", emp2.getLivro().getTitulo());
    }

    @Test
    void emprestimosIndependentes() {
        Usuario usuario2 = new Aluno("Pedro");
        Livro livro2 = new Livro("Design Patterns", "GoF");
        
        Emprestimo emp1 = new Emprestimo(usuario, livro);
        Emprestimo emp2 = new Emprestimo(usuario2, livro2);
        
        emp1.marcarDevolvido();
        
        assertEquals(Emprestimo.Status.DEVOLVIDO, emp1.getStatus());
        assertEquals(Emprestimo.Status.EMPRESTADO, emp2.getStatus());
        assertNotNull(emp1.getDataDevolucao());
        assertNull(emp2.getDataDevolucao());
    }
}
