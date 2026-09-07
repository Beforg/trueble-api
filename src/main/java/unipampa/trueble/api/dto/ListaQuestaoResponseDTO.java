package unipampa.trueble.api.dto;

import java.util.UUID;

public record ListaQuestaoResponseDTO(
        UUID listaQuestaoId,
        Integer ordem,
        QuestaoResponseDTO questao //
) {}