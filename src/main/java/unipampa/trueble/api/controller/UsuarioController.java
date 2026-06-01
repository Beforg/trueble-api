package unipampa.trueble.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unipampa.trueble.api.domain.Usuario;
import unipampa.trueble.api.dto.RegistroUsuarioDTO;
import unipampa.trueble.api.services.UsuarioService;

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
}