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
 * Integration tests for UsuarioRestController.
 */
@SpringBootTest
@AutoConfigureMockMvc
class UsuarioRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    void tearDown() {
        Biblioteca.resetInstancia();
    }

    @Test
    void deveListarUsuarios() throws Exception {
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].nome", notNullValue()))
                .andExpect(jsonPath("$[0].tipo", notNullValue()));
    }

    @Test
    void deveCadastrarNovoUsuario() throws Exception {
        String usuarioJson = """
                {
                    "nome": "Test User",
                    "tipo": "Aluno"
                }
                """;

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome", is("Test User")))
                .andExpect(jsonPath("$.tipo", is("Aluno")));
    }

    @Test
    void deveBuscarUsuarioPorNome() throws Exception {
        mockMvc.perform(get("/api/usuarios/Ana Souza"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Ana Souza")))
                .andExpect(jsonPath("$.tipo", is("Aluno")));
    }

    @Test
    void deveRetornarNotFoundQuandoUsuarioNaoExiste() throws Exception {
        mockMvc.perform(get("/api/usuarios/Nonexistent User"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", containsString("não encontrado")));
    }

    @Test
    void deveValidarCamposObrigatorios() throws Exception {
        String usuarioInvalido = """
                {
                    "nome": "",
                    "tipo": ""
                }
                """;

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioInvalido))
                .andExpect(status().isBadRequest());
    }
}
