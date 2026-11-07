package unipampa.trueble.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unipampa.trueble.api.domain.RespostaAluno;

import java.util.UUID;

public interface RespostaAlunoRepository extends JpaRepository<RespostaAluno, UUID> {
}
