package unipampa.trueble.api.dto;

import unipampa.trueble.api.domain.ListaDeQuestoes;
import java.util.UUID;

/**
 *
 * @param id ID da lista de Questões
 * @param titulo
 * @param descricao
 * @param nomeProfessor Levamos apenas o nome do professor.
 * @param totalQuestoes
 */
public record ListaDeQuestoesResponseDTO(
        UUID id,
        String titulo,
        String descricao,
        String nomeProfessor,
        Integer totalQuestoes
) {
    public ListaDeQuestoesResponseDTO(ListaDeQuestoes lista, Integer totalQuestoes) {
        this(
                lista.getId(),
                lista.getTitulo(),
                lista.getDescricao(),
                lista.getProfessor().getNome(),
                totalQuestoes
        );
    }
}