package unipampa.trueble.api.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Map;
import java.util.UUID;

public record RespostaAlunoRequestDTO(
        @NotNull UUID questaoId,
        @NotNull Map<String, Object> resposta,
        @NotNull Boolean correta
) {
}
