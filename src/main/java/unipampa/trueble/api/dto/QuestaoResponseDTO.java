package unipampa.trueble.api.dto;

import unipampa.trueble.api.domain.Questao;
import unipampa.trueble.api.enums.TipoQuestao;
import java.util.Map;
import java.util.UUID;

public record QuestaoResponseDTO(
        UUID id,
        String titulo,
        String descricao,
        TipoQuestao tipoQuestao,
        String categoria,
        Map<String, Object> conteudo,
        Boolean publico,
        String nomeProfessor // Trazemos apenas o Nome do professor
) {
    public QuestaoResponseDTO(Questao questao) {
        this(
                questao.getId(),
                questao.getTitulo(),
                questao.getDescricao(),
                questao.getTipoQuestao(),
                questao.getCategoria(),
                questao.getConteudo(),
                questao.getPublico(),
                questao.getProfessor().getNome()
        );
    }
}
