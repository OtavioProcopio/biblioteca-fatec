package com.fatec.biblioteca.api.controller;

import com.fatec.biblioteca.api.dto.UsuarioRequestDTO;
import com.fatec.biblioteca.api.dto.UsuarioResponseDTO;
import com.fatec.biblioteca.api.exception.ResourceNotFoundException;
import com.fatec.biblioteca.controller.BibliotecaController;
import com.fatec.biblioteca.factories.UsuarioFactory;
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
 * REST Controller for managing users.
 */
@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuários", description = "API para gerenciamento de usuários")
public class UsuarioRestController {

    private final BibliotecaController controller;

    public UsuarioRestController(BibliotecaController controller) {
        this.controller = controller;
    }

    @GetMapping
    @Operation(summary = "Listar todos os usuários", description = "Retorna a lista completa de usuários cadastrados")
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios() {
        List<UsuarioResponseDTO> usuarios = controller.listarUsuarios()
                .stream()
                .map(UsuarioResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{nome}")
    @Operation(summary = "Buscar usuário por nome", description = "Retorna um usuário específico pelo nome")
    public ResponseEntity<UsuarioResponseDTO> buscarUsuario(@PathVariable String nome) {
        Usuario usuario = controller.listarUsuarios()
                .stream()
                .filter(u -> u.getNome().equalsIgnoreCase(nome))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + nome));
        return ResponseEntity.ok(new UsuarioResponseDTO(usuario));
    }

    @PostMapping
    @Operation(summary = "Cadastrar novo usuário", description = "Adiciona um novo usuário ao sistema")
    public ResponseEntity<UsuarioResponseDTO> cadastrarUsuario(@Valid @RequestBody UsuarioRequestDTO request) {
        Usuario usuario = UsuarioFactory.criarUsuario(request.getTipo(), request.getNome());
        controller.cadastrarUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UsuarioResponseDTO(usuario));
    }
}
