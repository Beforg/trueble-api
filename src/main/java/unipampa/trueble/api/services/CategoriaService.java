package unipampa.trueble.api.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unipampa.trueble.api.domain.Categoria;
import unipampa.trueble.api.dto.CategoriaRequestDTO;
import unipampa.trueble.api.dto.CategoriaResponseDTO;
import unipampa.trueble.api.repository.CategoriaRepository;

import java.util.List;
import java.util.UUID;

@Service
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional
    public CategoriaResponseDTO criarCategoria(CategoriaRequestDTO dto) {
        Categoria novaCategoria = new Categoria(dto);
        categoriaRepository.save(novaCategoria);
        return new CategoriaResponseDTO(novaCategoria);
    }

    public List<CategoriaResponseDTO> listarCategorias() {
        return categoriaRepository.findAll().stream().map(CategoriaResponseDTO::new).toList();
    }

    public CategoriaResponseDTO atualizarCategoria(String id, CategoriaRequestDTO dto) {
        Categoria categoria = categoriaRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
        categoria.atualizarCategoria(dto);
        categoriaRepository.save(categoria);
        return new CategoriaResponseDTO(categoria);
    }
}
