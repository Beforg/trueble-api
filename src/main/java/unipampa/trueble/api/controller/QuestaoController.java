package unipampa.trueble.api.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import unipampa.trueble.api.enums.TipoQuestao;
import unipampa.trueble.api.services.QuestaoService;

import java.util.List;
import java.util.UUID;
import java.util.List;

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
            @Parameter(description = "Filtro parcial por título da questão (case-insensitive)")
            @RequestParam(required = false) String titulo,
            @Parameter(description = "Filtro exato pelo tipo da questão")
            @RequestParam(required = false) TipoQuestao tipoQuestao,
            @Parameter(description = "Filtro pelo ID único da categoria")
            @RequestParam(required = false) UUID categoriaId,
            @ParameterObject @PageableDefault(size = 10, sort = "criadoEm") Pageable pageable
    ) {
        Page<QuestaoResponseDTO> pagina = questaoService.listarBancoDeQuestoes(
                jwt, titulo, tipoQuestao, categoriaId, pageable
        );

        return ResponseEntity.ok(pagina);
    }
}