package unipampa.trueble.api.services;

import org.springframework.stereotype.Service;
import unipampa.trueble.api.domain.Categoria;
import unipampa.trueble.api.dto.CategoriaRequestDTO;
import unipampa.trueble.api.dto.CategoriaResponseDTO;
import unipampa.trueble.api.repository.CategoriaRepository;

@Service
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }
    public CategoriaResponseDTO criarCategoria(CategoriaRequestDTO dto) {
        Categoria novaCategoria = new Categoria(dto);
        categoriaRepository.save(novaCategoria);
        return new CategoriaResponseDTO(novaCategoria.getId(), novaCategoria.getNome());
    }
}
