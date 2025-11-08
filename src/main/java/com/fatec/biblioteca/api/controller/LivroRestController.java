package com.fatec.biblioteca.api.controller;

import com.fatec.biblioteca.api.dto.LivroRequestDTO;
import com.fatec.biblioteca.api.dto.LivroResponseDTO;
import com.fatec.biblioteca.api.exception.ResourceNotFoundException;
import com.fatec.biblioteca.controller.BibliotecaController;
import com.fatec.biblioteca.factories.LivroFactory;
import com.fatec.biblioteca.model.Livro;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for managing books.
 */
@RestController
@RequestMapping("/api/livros")
@Tag(name = "Livros", description = "API para gerenciamento de livros")
public class LivroRestController {

    private final BibliotecaController controller;

    public LivroRestController(BibliotecaController controller) {
        this.controller = controller;
    }

    @GetMapping
    @Operation(summary = "Listar todos os livros", description = "Retorna a lista completa de livros cadastrados")
    public ResponseEntity<List<LivroResponseDTO>> listarLivros() {
        List<LivroResponseDTO> livros = controller.listarLivros()
                .stream()
                .map(LivroResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(livros);
    }

    @GetMapping("/{titulo}")
    @Operation(summary = "Buscar livro por título", description = "Retorna um livro específico pelo título")
    public ResponseEntity<LivroResponseDTO> buscarLivro(@PathVariable String titulo) {
        Livro livro = controller.listarLivros()
                .stream()
                .filter(l -> l.getTitulo().equalsIgnoreCase(titulo))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado: " + titulo));
        return ResponseEntity.ok(new LivroResponseDTO(livro));
    }

    @PostMapping
    @Operation(summary = "Cadastrar novo livro", description = "Adiciona um novo livro ao acervo")
    public ResponseEntity<LivroResponseDTO> cadastrarLivro(@Valid @RequestBody LivroRequestDTO request) {
        Livro livro = LivroFactory.criarLivro(request.getTitulo(), request.getAutor());
        controller.cadastrarLivro(livro);
        return ResponseEntity.status(HttpStatus.CREATED).body(new LivroResponseDTO(livro));
    }

    @DeleteMapping("/{titulo}")
    @Operation(summary = "Remover livro", description = "Remove um livro do acervo")
    public ResponseEntity<Void> removerLivro(@PathVariable String titulo) {
        Livro livro = controller.listarLivros()
                .stream()
                .filter(l -> l.getTitulo().equalsIgnoreCase(titulo))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado: " + titulo));
        controller.removerLivro(livro);
        return ResponseEntity.noContent().build();
    }
}
