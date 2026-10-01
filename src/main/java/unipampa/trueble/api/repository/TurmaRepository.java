package unipampa.trueble.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import unipampa.trueble.api.domain.Turma;

import java.util.List;
import java.util.UUID;

public interface TurmaRepository extends JpaRepository<Turma, UUID> {

    @Query("SELECT t FROM Turma t JOIN FETCH t.professor WHERE t.professor.id = :professorId AND t.ativo = true")
    List<Turma> buscarTurmasDoProfessor(@Param("professorId") UUID professorId);

    @Query("SELECT t FROM AlunoTurma at JOIN at.turma t JOIN FETCH t.professor WHERE at.aluno.id = :alunoId AND t.ativo = true")
    List<Turma> buscarTurmasDoAluno(@Param("alunoId") UUID alunoId);
}