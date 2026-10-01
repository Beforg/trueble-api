package unipampa.trueble.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TurmaRequestDTO(
        @NotBlank(message = "O nome da turma não pode ser vazio.")
        String nomeTurma,
        @Max(value = 200, message = "A descrição da turma não pode exceder 200 caracteres.")
        String descricao,
        @NotNull
        LocalDate dataInicio,
        @NotNull
        LocalDate dataFim
) {
}
