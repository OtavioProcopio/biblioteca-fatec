package com.fatec.biblioteca.api.controller;

import com.fatec.biblioteca.api.dto.EmprestimoRequestDTO;
import com.fatec.biblioteca.api.dto.EmprestimoResponseDTO;
import com.fatec.biblioteca.api.exception.ResourceNotFoundException;
import com.fatec.biblioteca.controller.BibliotecaController;
import com.fatec.biblioteca.model.Emprestimo;
import com.fatec.biblioteca.model.Livro;
import com.fatec.biblioteca.model.usuarios.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for managing loans.
 */
@RestController
@RequestMapping("/api/emprestimos")
@Tag(name = "Empréstimos", description = "API para gerenciamento de empréstimos")
public class EmprestimoRestController {

    private final BibliotecaController controller;

    public EmprestimoRestController(BibliotecaController controller) {
        this.controller = controller;
    }

    @GetMapping
    @Operation(summary = "Listar todos os empréstimos", description = "Retorna a lista completa de empréstimos registrados")
    public ResponseEntity<List<EmprestimoResponseDTO>> listarEmprestimos() {
        List<EmprestimoResponseDTO> emprestimos = controller.listarEmprestimos()
                .stream()
                .map(EmprestimoResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(emprestimos);
    }

    @PostMapping
    @Operation(summary = "Registrar novo empréstimo", description = "Registra um novo empréstimo de livro")
    public ResponseEntity<EmprestimoResponseDTO> registrarEmprestimo(@Valid @RequestBody EmprestimoRequestDTO request) {
        Usuario usuario = controller.listarUsuarios()
                .stream()
                .filter(u -> u.getNome().equalsIgnoreCase(request.getNomeUsuario()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + request.getNomeUsuario()));

        Livro livro = controller.listarLivros()
                .stream()
                .filter(l -> l.getTitulo().equalsIgnoreCase(request.getTituloLivro()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado: " + request.getTituloLivro()));

        Emprestimo emprestimo = controller.registrarEmprestimo(usuario, livro);
        return ResponseEntity.status(HttpStatus.CREATED).body(new EmprestimoResponseDTO(emprestimo));
    }

    @PutMapping("/{nomeUsuario}/{tituloLivro}/devolver")
    @Operation(summary = "Registrar devolução", description = "Registra a devolução de um livro emprestado")
    public ResponseEntity<EmprestimoResponseDTO> registrarDevolucao(
            @PathVariable String nomeUsuario, 
            @PathVariable String tituloLivro) {
        
        Emprestimo emprestimo = controller.listarEmprestimos()
                .stream()
                .filter(e -> e.getUsuario().getNome().equalsIgnoreCase(nomeUsuario) 
                        && e.getLivro().getTitulo().equalsIgnoreCase(tituloLivro)
                        && e.getStatus() == Emprestimo.Status.EMPRESTADO)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Empréstimo ativo não encontrado para usuário " + nomeUsuario + " e livro " + tituloLivro));

        controller.registrarDevolucao(emprestimo);
        return ResponseEntity.ok(new EmprestimoResponseDTO(emprestimo));
    }
}
