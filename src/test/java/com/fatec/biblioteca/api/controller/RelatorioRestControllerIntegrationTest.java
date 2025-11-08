package com.fatec.biblioteca.api.controller;

import com.fatec.biblioteca.model.Biblioteca;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * Integration tests for RelatorioRestController.
 */
@SpringBootTest
@AutoConfigureMockMvc
class RelatorioRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    void tearDown() {
        Biblioteca.resetInstancia();
    }

    @Test
    void deveGerarRelatorioCompleto() throws Exception {
        mockMvc.perform(get("/api/relatorios"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Relatório da Biblioteca")))
                .andExpect(content().string(containsString("Livros:")))
                .andExpect(content().string(containsString("Usuários:")))
                .andExpect(content().string(containsString("Empréstimos:")));
    }
}
