package unipampa.trueble.api.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import unipampa.trueble.api.dto.QuestaoRequestDTO;
import unipampa.trueble.api.dto.QuestaoResponseDTO;
import unipampa.trueble.api.services.QuestaoService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.version}/questoes")
@Tag(name = "Questões", description = "Endpoints para gerenciamento do Banco de Questões")
public class QuestaoController {

    private final QuestaoService questaoService;

    public QuestaoController(QuestaoService questaoService) {
        this.questaoService = questaoService;
    }

    @PostMapping
    public ResponseEntity<QuestaoResponseDTO> criarQuestao(@AuthenticationPrincipal Jwt jwt, @RequestBody QuestaoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(questaoService.criarQuestao(jwt, dto));
    }

    @PutMapping("/{questaoId}")
    @Operation(
            summary = "Atualizar questão",
            description = "Atualiza os dados da questão. A categoria não pode ser alterada por este endpoint; " +
                    "o campo categoria enviado no corpo será ignorado. Apenas o professor proprietário pode editar."
    )
    @ApiResponse(responseCode = "200", description = "Questão atualizada com sucesso")
    @ApiResponse(responseCode = "403", description = "O usuário não é o proprietário da questão")
    @ApiResponse(responseCode = "404", description = "Questão não encontrada")
    public ResponseEntity<QuestaoResponseDTO> atualizarQuestao(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID questaoId,
            @RequestBody QuestaoRequestDTO dto) {
        return ResponseEntity.ok(questaoService.atualizarQuestao(jwt, questaoId, dto));
    }

    @DeleteMapping("/{questaoId}")
    @Operation(
            summary = "Excluir questão",
            description = "Desativa uma questão (exclusão lógica). Apenas o professor proprietário pode excluí-la."
    )
    @ApiResponse(responseCode = "204", description = "Questão excluída com sucesso")
    @ApiResponse(responseCode = "403", description = "O usuário não é o proprietário da questão")
    @ApiResponse(responseCode = "404", description = "Questão não encontrada")
    public ResponseEntity<Void> deletarQuestao(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID questaoId) {
        questaoService.deletarQuestao(jwt, questaoId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/minhas")
    public ResponseEntity<List<QuestaoResponseDTO>> listarMinhasQuestoes(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(questaoService.listarMinhasQuestoes(jwt));
    }

    @GetMapping("/publicas")
    public ResponseEntity<List<QuestaoResponseDTO>> listarQuestoesPublicas() {
        return ResponseEntity.ok(questaoService.listarQuestoesPublicas());
    }

    @GetMapping("/banco")
    @Operation(
            summary = "Listar Banco de Questões",
            description = "Retorna uma lista paginada de questões filtráveis. Inclui questões públicas e as criadas pelo próprio professor."
    )
    @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    public ResponseEntity<Page<QuestaoResponseDTO>> buscarBancoQuestoes(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(required = false) UUID categoriaId,
            @ParameterObject @PageableDefault(size = 10, sort = "criadoEm") Pageable pageable
    ) {
        Page<QuestaoResponseDTO> pagina = questaoService.listarBancoDeQuestoes(
                jwt, categoriaId, pageable
        );

        return ResponseEntity.ok(pagina);
    }
}