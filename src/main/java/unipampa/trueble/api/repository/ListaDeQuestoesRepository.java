package unipampa.trueble.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unipampa.trueble.api.domain.ListaDeQuestoes;
import java.util.List;
import java.util.UUID;

public interface ListaDeQuestoesRepository extends JpaRepository<ListaDeQuestoes, UUID> {
    List<ListaDeQuestoes> findAllByProfessorIdAndAtivoTrue(UUID professorId);
    List<ListaDeQuestoes> findAllByTurmaIdAndAtivoTrue(UUID turmaId);
}