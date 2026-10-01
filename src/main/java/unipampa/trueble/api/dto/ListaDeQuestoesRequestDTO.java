package unipampa.trueble.api.dto;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

/**
 *
 * @param titulo Título da Lista de Questões, ela não sabe das questões em si.
 * @param descricao
 * @param questoes Questões com a ordem.
 */
public record ListaDeQuestoesRequestDTO(
        String titulo,
        String descricao,
        @NotNull
        UUID turmaId,
        List<ListaQuestaoRequestDTO> questoes,
        LocalDate dataInicio,
        LocalDate dataFim
) {}