package unipampa.trueble.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import unipampa.trueble.api.dto.ListaDeQuestoesRequestDTO;
import unipampa.trueble.api.dto.ListaDeQuestoesResponseDTO;
import unipampa.trueble.api.services.ListaDeQuestoesService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.version}/listas")
public class ListaDeQuestoesController {

    private final ListaDeQuestoesService listaDeQuestoesService;

    public ListaDeQuestoesController(ListaDeQuestoesService listaDeQuestoesService) {
        this.listaDeQuestoesService = listaDeQuestoesService;
    }

    @PostMapping
    public ResponseEntity<ListaDeQuestoesResponseDTO> criarLista(@AuthenticationPrincipal Jwt jwt,
                                                                 @RequestBody ListaDeQuestoesRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(listaDeQuestoesService.criarLista(jwt, dto));
    }

    @GetMapping("/minhas")
    public ResponseEntity<List<ListaDeQuestoesResponseDTO>> listarMinhasListas(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(listaDeQuestoesService.listarMinhasListas(jwt));
    }

    @GetMapping("/turma/{turmaId}")
    public ResponseEntity<List<ListaDeQuestoesResponseDTO>> listarListasDaTurma(@PathVariable UUID turmaId) {
        return ResponseEntity.ok(listaDeQuestoesService.listarListasDaTurma(turmaId));
    }
}