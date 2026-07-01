package unipampa.trueble.api.dto;
import java.util.List;

/**
 *
 * @param titulo Título da Lista de Questões, ela não sabe das questões em si.
 * @param descricao
 * @param questoes Questões com a ordem.
 */
public record ListaDeQuestoesRequestDTO(
        String titulo,
        String descricao,
        List<ListaQuestaoRequestDTO> questoes
) {}