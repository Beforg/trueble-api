package unipampa.trueble.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unipampa.trueble.api.domain.Questao;
import java.util.List;
import java.util.UUID;

public interface QuestaoRepository extends JpaRepository<Questao, UUID> {

    List<Questao> findAllByPublicoTrueAndAtivoTrue();
    List<Questao> findAllByProfessorIdAndAtivoTrue(UUID professorId);
}