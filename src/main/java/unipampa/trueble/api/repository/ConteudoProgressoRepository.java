package unipampa.trueble.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unipampa.trueble.api.domain.ConteudoProgresso;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConteudoProgressoRepository extends JpaRepository<ConteudoProgresso, UUID> {
    List<ConteudoProgresso> findAllByAlunoId(UUID alunoId);

    Optional<ConteudoProgresso> findByAlunoIdAndConteudoId(UUID alunoId, Long conteudoId);
}

