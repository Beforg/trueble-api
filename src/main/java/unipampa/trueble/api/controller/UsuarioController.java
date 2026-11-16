package unipampa.trueble.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import unipampa.trueble.api.domain.Usuario;
import unipampa.trueble.api.dto.AvatarUploadRequestDTO;
import unipampa.trueble.api.dto.AvatarUploadResponseDTO;
import unipampa.trueble.api.dto.AvatarUpdateRequestDTO;
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

    @PostMapping("/perfil/avatar/upload-url")
    @Operation(summary = "Gerar URL de upload do avatar")
    public ResponseEntity<AvatarUploadResponseDTO> gerarUrlUploadAvatar(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid AvatarUploadRequestDTO dto) {
        UUID usuarioId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(
                usuarioService.criarUrlUploadAvatar(usuarioId, dto.contentType()));
    }

    @PatchMapping("/perfil/avatar")
    @Operation(summary = "Salvar avatar do perfil")
    public ResponseEntity<UsuarioResponseDTO> salvarAvatar(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody AvatarUpdateRequestDTO dto) {
        UUID usuarioId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(usuarioService.atualizarAvatar(usuarioId, dto));
    }
}