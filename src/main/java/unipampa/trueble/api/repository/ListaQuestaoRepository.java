package unipampa.trueble.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unipampa.trueble.api.domain.ListaQuestao;
import java.util.UUID;

public interface ListaQuestaoRepository extends JpaRepository<ListaQuestao, UUID> {
    Integer countByListaId(UUID listaId);
    boolean existsByListaIdAndQuestaoId(UUID listaId, UUID questaoId);
    void deleteByListaIdAndQuestaoId(UUID listaId, UUID questaoId);
}