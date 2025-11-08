package com.fatec.biblioteca.api.controller;

import com.fatec.biblioteca.controller.BibliotecaController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for generating reports.
 */
@RestController
@RequestMapping("/api/relatorios")
@Tag(name = "Relatórios", description = "API para geração de relatórios")
public class RelatorioRestController {

    private final BibliotecaController controller;

    public RelatorioRestController(BibliotecaController controller) {
        this.controller = controller;
    }

    @GetMapping
    @Operation(summary = "Gerar relatório completo", description = "Retorna um relatório completo da biblioteca")
    public ResponseEntity<String> gerarRelatorio() {
        String relatorio = controller.gerarRelatorio();
        return ResponseEntity.ok(relatorio);
    }
}
