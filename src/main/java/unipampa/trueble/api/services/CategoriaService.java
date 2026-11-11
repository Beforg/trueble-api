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

        // Verifica se foi informada uma categoria pai
        if (dto.categoriaPaiId() != null) {

            Categoria categoriaPai = categoriaRepository
                    .findById(dto.categoriaPaiId())
                    .orElseThrow(() -> new RuntimeException(
                            "Categoria pai não encontrada"
                    ));

            novaCategoria.setCategoriaPai(categoriaPai);
        }

        categoriaRepository.save(novaCategoria);

        return new CategoriaResponseDTO(novaCategoria);
    }

    public List<CategoriaResponseDTO> listarCategorias() {
        return categoriaRepository.findAll()
                .stream()
                .map(CategoriaResponseDTO::new)
                .toList();
    }

    @Transactional
    public CategoriaResponseDTO atualizarCategoria(String id, CategoriaRequestDTO dto) {

        Categoria categoria = categoriaRepository
                .findById(UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException(
                        "Categoria não encontrada"
                ));

        categoria.atualizarCategoria(dto);

        // Atualiza a categoria pai
        if (dto.categoriaPaiId() != null) {

            if (categoria.getId().equals(dto.categoriaPaiId())) {
                throw new RuntimeException(
                        "Uma categoria não pode ser sua própria categoria pai"
                );
            }

            Categoria categoriaPai = categoriaRepository
                    .findById(dto.categoriaPaiId())
                    .orElseThrow(() -> new RuntimeException(
                            "Categoria pai não encontrada"
                    ));

            categoria.setCategoriaPai(categoriaPai);

        } else {
            // Null significa que a categoria será categoria pai, então removemos a referência à categoria pai
            categoria.setCategoriaPai(null);
        }

        categoriaRepository.save(categoria);

        return new CategoriaResponseDTO(categoria);
    }
}
