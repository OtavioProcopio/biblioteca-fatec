package com.fatec.biblioteca.api.controller;

import com.fatec.biblioteca.model.Biblioteca;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * Integration tests for EmprestimoRestController.
 */
@SpringBootTest
@AutoConfigureMockMvc
class EmprestimoRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    void tearDown() {
        Biblioteca.resetInstancia();
    }

    @Test
    void deveListarEmprestimos() throws Exception {
        mockMvc.perform(get("/api/emprestimos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(java.util.List.class)));
    }

    @Test
    void deveRegistrarNovoEmprestimo() throws Exception {
        String emprestimoJson = """
                {
                    "nomeUsuario": "Ana Souza",
                    "tituloLivro": "Clean Code"
                }
                """;

        mockMvc.perform(post("/api/emprestimos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emprestimoJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nomeUsuario", is("Ana Souza")))
                .andExpect(jsonPath("$.tituloLivro", is("Clean Code")))
                .andExpect(jsonPath("$.status", is("EMPRESTADO")));
    }

    @Test
    void deveRegistrarDevolucao() throws Exception {
        // First, register a loan
        String emprestimoJson = """
                {
                    "nomeUsuario": "Bruno Lima",
                    "tituloLivro": "1984"
                }
                """;

        mockMvc.perform(post("/api/emprestimos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(emprestimoJson));

        // Then return it
        mockMvc.perform(put("/api/emprestimos/Bruno Lima/1984/devolver"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("DEVOLVIDO")));
    }

    @Test
    void deveRetornarNotFoundQuandoEmprestimoNaoExiste() throws Exception {
        mockMvc.perform(put("/api/emprestimos/Nonexistent User/Nonexistent Book/devolver"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", containsString("não encontrado")));
    }

    @Test
    void deveRetornarConflictQuandoLivroIndisponivel() throws Exception {
        // Register a loan to make the book unavailable
        String emprestimoJson = """
                {
                    "nomeUsuario": "Carla Mendes",
                    "tituloLivro": "O Pequeno Príncipe"
                }
                """;

        mockMvc.perform(post("/api/emprestimos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(emprestimoJson));

        // Try to loan the same book again
        String emprestimoJson2 = """
                {
                    "nomeUsuario": "Diego Ferreira",
                    "tituloLivro": "O Pequeno Príncipe"
                }
                """;

        mockMvc.perform(post("/api/emprestimos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emprestimoJson2))
                .andExpect(status().isConflict());
    }

    @Test
    void deveValidarCamposObrigatorios() throws Exception {
        String emprestimoInvalido = """
                {
                    "nomeUsuario": "",
                    "tituloLivro": ""
                }
                """;

        mockMvc.perform(post("/api/emprestimos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emprestimoInvalido))
                .andExpect(status().isBadRequest());
    }
}
