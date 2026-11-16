package unipampa.trueble.api.dto;

import jakarta.validation.constraints.NotBlank;

public record AvatarUploadRequestDTO(
        @NotBlank String contentType
) {
}
