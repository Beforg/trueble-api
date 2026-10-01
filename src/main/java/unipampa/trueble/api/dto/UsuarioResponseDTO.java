package unipampa.trueble.api.dto;

import unipampa.trueble.api.domain.Usuario;
import java.util.UUID;

public record UsuarioResponseDTO(
        UUID id,
        String nome,
        String email,
        String role
) {
    public UsuarioResponseDTO(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                formatarRole(usuario.getRole().name()) // para o formato  do Frontend
        );
    }

    private static String formatarRole(String role) {
        if (role == null || role.isEmpty()) return role;
        return role.substring(0, 1).toUpperCase() + role.substring(1).toLowerCase();
    }
}