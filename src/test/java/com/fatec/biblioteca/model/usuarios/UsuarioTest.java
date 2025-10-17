package com.fatec.biblioteca.model.usuarios;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UsuarioTest {

    @Test
    void criarAlunoComSucesso() {
        Usuario aluno = new Aluno("Maria Silva");
        
        assertNotNull(aluno);
        assertEquals("Maria Silva", aluno.getNome());
        assertTrue(aluno instanceof Aluno);
    }

    @Test
    void criarProfessorComSucesso() {
        Usuario professor = new Professor("Carlos Souza");
        
        assertNotNull(professor);
        assertEquals("Carlos Souza", professor.getNome());
        assertTrue(professor instanceof Professor);
    }

    @Test
    void criarBibliotecarioComSucesso() {
        Usuario bibliotecario = new Bibliotecario("Ana Paula");
        
        assertNotNull(bibliotecario);
        assertEquals("Ana Paula", bibliotecario.getNome());
        assertTrue(bibliotecario instanceof Bibliotecario);
    }

    @Test
    void alunoToStringContemNomeETipo() {
        Usuario aluno = new Aluno("João");
        
        String resultado = aluno.toString();
        
        assertNotNull(resultado);
        assertTrue(resultado.contains("Aluno"));
        assertTrue(resultado.contains("João"));
    }

    @Test
    void professorToStringContemNomeETipo() {
        Usuario professor = new Professor("Pedro");
        
        String resultado = professor.toString();
        
        assertNotNull(resultado);
        assertTrue(resultado.contains("Professor"));
        assertTrue(resultado.contains("Pedro"));
    }

    @Test
    void bibliotecarioToStringContemNomeETipo() {
        Usuario bibliotecario = new Bibliotecario("Rita");
        
        String resultado = bibliotecario.toString();
        
        assertNotNull(resultado);
        assertTrue(resultado.contains("Bibliotecario"));
        assertTrue(resultado.contains("Rita"));
    }

    @Test
    void alunoGetNomeRetornaNomeCorreto() {
        Usuario aluno = new Aluno("Lucas Mendes");
        
        assertEquals("Lucas Mendes", aluno.getNome());
    }

    @Test
    void professorGetNomeRetornaNomeCorreto() {
        Usuario professor = new Professor("Fernanda Costa");
        
        assertEquals("Fernanda Costa", professor.getNome());
    }

    @Test
    void bibliotecarioGetNomeRetornaNomeCorreto() {
        Usuario bibliotecario = new Bibliotecario("Roberto Lima");
        
        assertEquals("Roberto Lima", bibliotecario.getNome());
    }

    @Test
    void usuariosComMesmoNomeSaoDiferentes() {
        Usuario aluno = new Aluno("José");
        Usuario professor = new Professor("José");
        Usuario bibliotecario = new Bibliotecario("José");
        
        assertEquals("José", aluno.getNome());
        assertEquals("José", professor.getNome());
        assertEquals("José", bibliotecario.getNome());
        
        assertNotSame(aluno, professor);
        assertNotSame(aluno, bibliotecario);
        assertNotSame(professor, bibliotecario);
    }

    @Test
    void usuariosComNomesVazios() {
        Usuario aluno = new Aluno("");
        Usuario professor = new Professor("");
        Usuario bibliotecario = new Bibliotecario("");
        
        assertEquals("", aluno.getNome());
        assertEquals("", professor.getNome());
        assertEquals("", bibliotecario.getNome());
    }

    @Test
    void usuariosComNomesEspeciais() {
        Usuario aluno = new Aluno("José da Silva Jr.");
        Usuario professor = new Professor("Dr. Carlos");
        Usuario bibliotecario = new Bibliotecario("Maria-José O'Connor");
        
        assertEquals("José da Silva Jr.", aluno.getNome());
        assertEquals("Dr. Carlos", professor.getNome());
        assertEquals("Maria-José O'Connor", bibliotecario.getNome());
    }

    @Test
    void usuariosComNomesLongos() {
        String nomeLongo = "Maria do Carmo da Silva Santos de Oliveira Pereira";
        Usuario aluno = new Aluno(nomeLongo);
        
        assertEquals(nomeLongo, aluno.getNome());
    }

    @Test
    void toStringDeUsuariosDiferentes() {
        Usuario aluno = new Aluno("Test");
        Usuario professor = new Professor("Test");
        Usuario bibliotecario = new Bibliotecario("Test");
        
        String strAluno = aluno.toString();
        String strProfessor = professor.toString();
        String strBibliotecario = bibliotecario.toString();
        
        assertTrue(strAluno.contains("Aluno"));
        assertTrue(strProfessor.contains("Professor"));
        assertTrue(strBibliotecario.contains("Bibliotecario"));
        
        assertNotEquals(strAluno, strProfessor);
        assertNotEquals(strAluno, strBibliotecario);
        assertNotEquals(strProfessor, strBibliotecario);
    }

    @Test
    void herancaCorreita() {
        Usuario aluno = new Aluno("Test");
        Usuario professor = new Professor("Test");
        Usuario bibliotecario = new Bibliotecario("Test");
        
        assertTrue(aluno instanceof Usuario);
        assertTrue(professor instanceof Usuario);
        assertTrue(bibliotecario instanceof Usuario);
    }

    @Test
    void tiposEspecificosCorretos() {
        Usuario aluno = new Aluno("Test");
        Usuario professor = new Professor("Test");
        Usuario bibliotecario = new Bibliotecario("Test");
        
        assertFalse(aluno instanceof Professor);
        assertFalse(aluno instanceof Bibliotecario);
        
        assertFalse(professor instanceof Aluno);
        assertFalse(professor instanceof Bibliotecario);
        
        assertFalse(bibliotecario instanceof Aluno);
        assertFalse(bibliotecario instanceof Professor);
    }
}
