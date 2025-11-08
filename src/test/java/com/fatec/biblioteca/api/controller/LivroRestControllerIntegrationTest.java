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
 * Integration tests for LivroRestController.
 */
@SpringBootTest
@AutoConfigureMockMvc
class LivroRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    void tearDown() {
        Biblioteca.resetInstancia();
    }

    @Test
    void deveListarLivros() throws Exception {
        mockMvc.perform(get("/api/livros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].titulo", notNullValue()))
                .andExpect(jsonPath("$[0].autor", notNullValue()));
    }

    @Test
    void deveCadastrarNovoLivro() throws Exception {
        String livroJson = """
                {
                    "titulo": "Test Book",
                    "autor": "Test Author"
                }
                """;

        mockMvc.perform(post("/api/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(livroJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo", is("Test Book")))
                .andExpect(jsonPath("$.autor", is("Test Author")))
                .andExpect(jsonPath("$.quantidade", is(1)))
                .andExpect(jsonPath("$.disponivel", is(true)));
    }

    @Test
    void deveBuscarLivroPorTitulo() throws Exception {
        mockMvc.perform(get("/api/livros/Clean Code"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo", is("Clean Code")))
                .andExpect(jsonPath("$.autor", is("Robert C. Martin")));
    }

    @Test
    void deveRetornarNotFoundQuandoLivroNaoExiste() throws Exception {
        mockMvc.perform(get("/api/livros/Nonexistent Book"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", containsString("não encontrado")));
    }

    @Test
    void deveRemoverLivro() throws Exception {
        mockMvc.perform(delete("/api/livros/Clean Code"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveValidarCamposObrigatorios() throws Exception {
        String livroInvalido = """
                {
                    "titulo": "",
                    "autor": ""
                }
                """;

        mockMvc.perform(post("/api/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(livroInvalido))
                .andExpect(status().isBadRequest());
    }
}
