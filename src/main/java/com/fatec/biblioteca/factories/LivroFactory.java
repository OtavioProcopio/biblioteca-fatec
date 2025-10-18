package com.fatec.biblioteca.factories;

import com.fatec.biblioteca.model.Livro;

/**
 * Factory para criar instâncias de Livro.
 */
public final class LivroFactory {
    private LivroFactory() {}

    public static Livro criarLivro(String titulo, String autor) {
        return new Livro(titulo, autor);
    }
}
