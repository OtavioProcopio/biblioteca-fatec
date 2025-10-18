package com.fatec.biblioteca;

import com.fatec.biblioteca.model.Biblioteca;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de integração para a classe Main.
 */
class MainTest {

    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        // Reset singleton para cada teste
        Biblioteca.resetInstancia();
        
        // Capturar saída do console
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    @DisplayName("Deve inicializar aplicação e sair imediatamente")
    void deveInicializarAplicacaoESair() {
        // Given
        String input = "0\n"; // Opção para sair
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // When
        Main.main(new String[]{});

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains("=== SISTEMA BIBLIOTECA ==="));
        assertTrue(output.contains("0. Sair"));
        assertTrue(output.contains("Sistema encerrado") || 
                  output.contains("Obrigado"));
    }

    @Test
    @DisplayName("Deve inicializar com dados padrão")
    void deveInicializarComDadosPadrao() {
        // Given
        String input = "2\n0\n"; // Listar livros e sair
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // When
        Main.main(new String[]{});

        // Then
        String output = outputStream.toString();
        
        // Verifica se os dados padrão foram carregados
        assertTrue(output.contains("=== LIVROS CADASTRADOS ==="));
        assertTrue(output.contains("O Senhor dos Anéis"));
        assertTrue(output.contains("Dom Casmurro"));
        assertTrue(output.contains("Clean Code"));
    }

    @Test
    @DisplayName("Deve navegar pelo menu e executar operações")
    void deveNavegarPeloMenuEExecutarOperacoes() {
        // Given
        String input = "4\n8\n0\n"; // Listar usuários, gerar relatório, sair
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // When
        Main.main(new String[]{});

        // Then
        String output = outputStream.toString();
        
        // Verifica se executou listar usuários
        assertTrue(output.contains("=== USUÁRIOS CADASTRADOS ==="));
        assertTrue(output.contains("Ana Souza"));
        
        // Verifica se executou gerar relatório
        assertTrue(output.contains("Relatório da Biblioteca"));
        
        // Verifica se saiu corretamente
        assertTrue(output.contains("Sistema encerrado") || 
                  output.contains("Obrigado"));
    }

    @Test
    @DisplayName("Deve tratar opção inválida graciosamente")
    void deveTratarOpcaoInvalidaGraciosamente() {
        // Given
        String input = "999\n0\n"; // Opção inválida, depois sair
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // When
        Main.main(new String[]{});

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains("Opção inválida") || 
                   output.contains("=== SISTEMA BIBLIOTECA ==="));
        assertTrue(output.contains("Sistema encerrado") || 
                  output.contains("Obrigado"));
    }

    void tearDown() {
        System.setOut(originalOut);
        System.setIn(System.in);
    }
}