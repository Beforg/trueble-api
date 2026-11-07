package unipampa.trueble.api.dto;

import unipampa.trueble.api.domain.RespostaAluno;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record RespostaAlunoResponseDTO(
        UUID id,
        UUID alunoId,
        UUID listaId,
        UUID questaoId,
        Map<String, Object> resposta,
        Boolean correta,
        Double nota,
        LocalDateTime respondidaEm
) {
    public RespostaAlunoResponseDTO(RespostaAluno respostaAluno) {
        this(
                respostaAluno.getId(),
                respostaAluno.getAluno().getId(),
                respostaAluno.getLista().getId(),
                respostaAluno.getQuestao().getId(),
                respostaAluno.getResposta(),
                respostaAluno.getCorreta(),
                respostaAluno.getNota(),
                respostaAluno.getRespondidaEm()
        );
    }
}
