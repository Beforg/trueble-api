package unipampa.trueble.api.dto;

import unipampa.trueble.api.enums.TipoQuestao;
import java.util.Map;

public record QuestaoRequestDTO(
        String titulo,
        String descricao,
        TipoQuestao tipoQuestao,
        String categoria,
        Map<String, Object> conteudo,
        Boolean publico
) {}
