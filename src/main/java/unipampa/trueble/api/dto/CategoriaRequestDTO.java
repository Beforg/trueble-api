package unipampa.trueble.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID; // para as subcategorias

@Schema(description = "Objeto de requisição para criação de uma nova Categoria")
public record CategoriaRequestDTO(
        @NotBlank
        @Schema(
                description = "Nome da categoria a ser criada.",
                example = "Semântica",
                required = true
        )
        String nome,

        @Schema(
        description = "UUID da categoria pai. Nulo se for categoria pai",
        example = "550e8400-e29b-41d4-a716-446655440000",
        nullable = true
        )
        UUID categoriaPaiId
        

) {
}
