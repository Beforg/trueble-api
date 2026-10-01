package unipampa.trueble.api.dto;

import unipampa.trueble.api.domain.Categoria;

import java.util.UUID;

public record CategoriaResponseDTO(
        UUID id,
        String nome
) {
    public CategoriaResponseDTO(Categoria categoria) {
        this(categoria.getId(), categoria.getNome());
    }
}
