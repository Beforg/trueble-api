package unipampa.trueble.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import unipampa.trueble.api.dto.QuestaoRequestDTO;
import unipampa.trueble.api.dto.QuestaoResponseDTO;
import unipampa.trueble.api.services.QuestaoService;

import java.util.List;

@RestController
@RequestMapping("${api.version}/questoes")
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
}