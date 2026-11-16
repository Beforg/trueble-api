package unipampa.trueble.api.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import unipampa.trueble.api.dto.CategoriaRequestDTO;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

@Data
@Entity
@Table(name = "categorias")
@AllArgsConstructor
@NoArgsConstructor
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, unique = true, length = 100)
    private String nome;

    //categoria pai
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_pai_id")
    private Categoria categoriaPai;

    //subcategoria
    @OneToMany(mappedBy = "categoriaPai")
    private List<Categoria> subCategorias = new ArrayList<>();

    public Categoria(CategoriaRequestDTO dto) {
        this.nome = dto.nome();
    }

    public void atualizarCategoria(CategoriaRequestDTO dto) {
        this.nome = dto.nome();
    }
}