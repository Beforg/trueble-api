package unipampa.trueble.api.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import unipampa.trueble.api.dto.RespostaAlunoRequestDTO;
import unipampa.trueble.api.dto.RespostaAlunoResponseDTO;
import unipampa.trueble.api.services.RespostaAlunoService;

import java.util.UUID;

@RestController
@RequestMapping("${api.version}/listas/{listaId}/respostas")
public class RespostaAlunoController {

    private final RespostaAlunoService respostaAlunoService;

    public RespostaAlunoController(RespostaAlunoService respostaAlunoService) {
        this.respostaAlunoService = respostaAlunoService;
    }

    @PostMapping
    public ResponseEntity<RespostaAlunoResponseDTO> criarResposta(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID listaId,
            @RequestBody @Valid RespostaAlunoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(respostaAlunoService.criarResposta(jwt, listaId, dto));
    }
}
