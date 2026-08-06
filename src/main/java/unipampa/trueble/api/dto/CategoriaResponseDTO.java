package unipampa.trueble.api.dto;

import java.util.UUID;

public record CategoriaResponseDTO(
        UUID id,
        String nome
) {
}
