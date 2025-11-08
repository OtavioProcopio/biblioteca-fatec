package com.fatec.biblioteca.config;

import com.fatec.biblioteca.controller.BibliotecaController;
import com.fatec.biblioteca.model.Biblioteca;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration class for the application.
 */
@Configuration
public class BibliotecaConfig {

    @Bean
    public Biblioteca biblioteca() {
        return Biblioteca.getInstancia();
    }

    @Bean
    public BibliotecaController bibliotecaController(Biblioteca biblioteca) {
        return new BibliotecaController(biblioteca);
    }
}
