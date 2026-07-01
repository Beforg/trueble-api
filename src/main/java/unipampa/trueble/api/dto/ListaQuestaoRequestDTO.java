package unipampa.trueble.api.dto;
import java.util.UUID;
/**
 * Responsável por saber quem é a questão (UUID)
 * */
public record ListaQuestaoRequestDTO(
        UUID questaoId,
        Integer ordem
) {}