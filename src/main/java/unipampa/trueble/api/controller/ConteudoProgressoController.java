package unipampa.trueble.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import unipampa.trueble.api.dto.ProgressoDTO;
import unipampa.trueble.api.services.ConteudoProgressoService;
import java.util.List;

@RestController
@RequestMapping("${api.version}/progresso")
public class ConteudoProgressoController {

    private final ConteudoProgressoService conteudoProgressoService;

    public ConteudoProgressoController(ConteudoProgressoService conteudoProgressoService) {
        this.conteudoProgressoService = conteudoProgressoService;
    }

    @GetMapping
    public ResponseEntity<List<ProgressoDTO>> listarMeusProgressos(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(conteudoProgressoService.listarMeusProgressos(jwt));
    }

    // 2. SALVAR OU ATUALIZAR PROGRESSO
    @PutMapping
    public ResponseEntity<ProgressoDTO> salvarProgresso(@AuthenticationPrincipal Jwt jwt, @RequestBody ProgressoDTO dto) {
        return ResponseEntity.ok(conteudoProgressoService.atualizarProgresso(jwt, dto));
    }
}
