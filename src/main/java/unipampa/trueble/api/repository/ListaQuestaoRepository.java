package unipampa.trueble.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import unipampa.trueble.api.domain.ListaQuestao;

import java.util.List;
import java.util.UUID;

public interface ListaQuestaoRepository extends JpaRepository<ListaQuestao, UUID> {
    Integer countByListaId(UUID listaId);
    @Query("""
        SELECT lq FROM ListaQuestao lq
        JOIN FETCH lq.questao q
        WHERE lq.lista.id = :listaId
        ORDER BY lq.ordem ASC
    """)
    List<ListaQuestao> buscarComQuestoesPorListaId(@Param("listaId") UUID listaId);
}