package unipampa.trueble.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unipampa.trueble.api.domain.AlunoTurma;
import java.util.UUID;

public interface AlunoTurmaRepository extends JpaRepository<AlunoTurma, UUID> {

    boolean existsByAlunoIdAndTurmaId(UUID alunoId, UUID turmaId);
}