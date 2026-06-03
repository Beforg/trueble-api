package unipampa.trueble.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import unipampa.trueble.api.domain.Role;

import java.util.UUID;

public record RegistroUsuarioDTO(
        @NotBlank
        UUID id,
        @Email
        @NotBlank
        String email,
        @NotBlank
        String nome,
        @NotBlank
        Role role) {
}
