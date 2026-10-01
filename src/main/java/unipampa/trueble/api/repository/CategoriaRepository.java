package unipampa.trueble.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unipampa.trueble.api.domain.Categoria;

import java.util.Optional;
import java.util.UUID;

public interface CategoriaRepository extends JpaRepository<Categoria, UUID> {
    Optional<Categoria> findByNome(String categoria);
}