package unipampa.trueble.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import unipampa.trueble.api.domain.Usuario;
import unipampa.trueble.api.dto.RegistroUsuarioDTO;
import unipampa.trueble.api.dto.UsuarioResponseDTO;
import unipampa.trueble.api.services.UsuarioService;

import java.util.UUID;

@RestController
@RequestMapping("${api.version}/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registro")
    public ResponseEntity<Usuario> registrar(@RequestBody RegistroUsuarioDTO dto) {
        Usuario novoUsuario = usuarioService.registrarNovoUsuario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    @GetMapping("/perfil")
    @Operation(summary = "Obter Perfil", description = "Retorna os dados cadastrais do usuário atualmente logado.")
    public ResponseEntity<UsuarioResponseDTO> obterPerfil(@AuthenticationPrincipal Jwt jwt) {
        UUID usuarioId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(usuarioService.obterPerfilAutenticado(usuarioId));
    }
}