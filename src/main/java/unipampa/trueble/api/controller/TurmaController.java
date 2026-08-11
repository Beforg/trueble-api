package unipampa.trueble.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.version}/turmas")
@Tag(name = "Turmas", description = "Endpoints para gerenciamento de Turmas")
public class TurmaController {

    @PostMapping
    @Operation(summary = "Criar Turma", description = "Cria uma nova turma no sistema.")
    public String criarTurma(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody ) {
        // Lógica para criar uma turma
    }
}
