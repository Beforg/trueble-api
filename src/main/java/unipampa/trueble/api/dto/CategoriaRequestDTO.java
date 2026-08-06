package unipampa.trueble.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Objeto de requisição para criação de uma nova Categoria")
public record CategoriaRequestDTO(
        @NotBlank
        @Schema(
                description = "Nome da categoria a ser criada.",
                example = "Semântica",
                required = true
        )
        String nome
) {
}
