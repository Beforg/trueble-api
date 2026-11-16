package unipampa.trueble.api.dto;

import unipampa.trueble.api.domain.Usuario;

import java.time.LocalDateTime;
import java.util.UUID;

public record UsuarioResponseDTO(
        UUID id,
        String nome,
        String email,
        String role,
        boolean ativo,
        LocalDateTime dataCadastro,
        String avatarPath
) {
    public UsuarioResponseDTO(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                formatarRole(usuario.getRole()),
                usuario.isAtivo(),
                usuario.getDataCadastro(),
                usuario.getAvatarPath()
        );
    }

    private static String formatarRole(Enum<?> role) {
        if (role == null) {
            return null;
        }

        String valor = role.name();
        return valor.substring(0, 1).toUpperCase() + valor.substring(1).toLowerCase();
    }
}