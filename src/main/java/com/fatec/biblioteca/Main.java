package com.fatec.biblioteca;

import com.fatec.biblioteca.controller.BibliotecaController;
import com.fatec.biblioteca.model.Biblioteca;
import com.fatec.biblioteca.view.BibliotecaView;

/**
 * Ponto de entrada da aplicação.
 */
public class Main {
    public static void main(String[] args) {
        Biblioteca repository = Biblioteca.getInstancia();
        BibliotecaController controller = new BibliotecaController(repository);
        BibliotecaView view = new BibliotecaView(controller);
        view.iniciar();
    }
}