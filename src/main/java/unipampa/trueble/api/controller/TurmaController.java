package unipampa.trueble.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import unipampa.trueble.api.dto.TurmaRequestDTO;
import unipampa.trueble.api.dto.TurmaResponseDTO;
import unipampa.trueble.api.services.TurmaService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.version}/turmas")
@Tag(name = "Turmas", description = "Endpoints para gerenciamento de Turmas")
public class TurmaController {

    private final TurmaService turmaService;

    public TurmaController(TurmaService turmaService) {
        this.turmaService = turmaService;
    }

    @PostMapping
    @Operation(summary = "Criar Turma", description = "Cria uma nova turma no sistema (Uso exclusivo de Professor).")
    public ResponseEntity<TurmaResponseDTO> criarTurma(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid TurmaRequestDTO dto) {

        UUID professorId = UUID.fromString(jwt.getSubject());
        TurmaResponseDTO response = turmaService.criarTurma(professorId, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar Turma", description = "Edita os dados de uma turma existente. Apenas o professor dono pode editar.")
    public ResponseEntity<TurmaResponseDTO> editarTurma(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID id,
            @RequestBody @Valid TurmaRequestDTO dto) {

        UUID professorId = UUID.fromString(jwt.getSubject());
        TurmaResponseDTO response = turmaService.editarTurma(professorId, id, dto);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/professor")
    @Operation(summary = "Listar Turmas do Professor", description = "Lista todas as turmas criadas pelo professor autenticado.")
    public ResponseEntity<List<TurmaResponseDTO>> listarTurmasDoProfessor(@AuthenticationPrincipal Jwt jwt) {
        UUID professorId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(turmaService.listarMinhasTurmasComoProfessor(professorId));
    }

    @GetMapping("/aluno")
    @Operation(summary = "Listar Turmas do Aluno", description = "Lista todas as turmas onde o aluno autenticado está matriculado.")
    public ResponseEntity<List<TurmaResponseDTO>> listarTurmasDoAluno(@AuthenticationPrincipal Jwt jwt) {
        UUID alunoId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(turmaService.listarMinhasTurmasComoAluno(alunoId));
    }

    @PostMapping("/{id}/matricular")
    @Operation(summary = "Matricular Aluno", description = "Matricula o aluno autenticado em uma turma específica.")
    public ResponseEntity<Void> matricular(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable("id") UUID turmaId) {

        UUID alunoId = UUID.fromString(jwt.getSubject());
        turmaService.matricularAluno(alunoId, turmaId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}