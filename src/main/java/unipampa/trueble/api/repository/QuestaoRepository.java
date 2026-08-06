package unipampa.trueble.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import unipampa.trueble.api.domain.Questao;
import unipampa.trueble.api.enums.TipoQuestao;

import java.util.List;
import java.util.UUID;

public interface QuestaoRepository extends JpaRepository<Questao, UUID> {

    List<Questao> findAllByPublicoTrueAndAtivoTrue();
    List<Questao> findAllByProfessorIdAndAtivoTrue(UUID professorId);
    @Query("""
        SELECT q FROM Questao q 
        JOIN q.categoria c
        WHERE q.ativo = true
        AND (:titulo IS NULL OR q.titulo ILIKE CONCAT('%', :titulo, '%'))
        AND (:tipoQuestao IS NULL OR q.tipoQuestao = :tipoQuestao)
        AND (:categoriaId IS NULL OR c.id = :categoriaId)
        AND (q.publico = true OR q.professor.id = :professorId) 
    """)
    Page<Questao> buscarBancoDeQuestoes(
            @Param("titulo") String titulo,
            @Param("tipoQuestao") TipoQuestao tipoQuestao,
            @Param("categoriaId") UUID categoriaId,
            @Param("professorId") UUID professorId,
            Pageable pageable
    );
}