package unipampa.trueble.api.dto;

import unipampa.trueble.api.domain.Professor;
import unipampa.trueble.api.domain.Turma;

import java.time.LocalDate;
import java.util.UUID;

public record TurmaResponseDTO(
        UUID id ,
        String nomeProfessor,
        String descricao,
        LocalDate dataInicio,
        LocalDate dataFim
) {
    public TurmaResponseDTO (Turma turma) {
        this(
                turma.getId(),
                turma.getProfessor().getNome(),
                turma.getDescricao(),
                turma.getDataInicio(),
                turma.getDataFim()
        );
    }
}
