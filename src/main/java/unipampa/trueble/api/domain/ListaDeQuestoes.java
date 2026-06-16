package unipampa.trueble.api.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "listas_de_questoes")
public class ListaDeQuestoes {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    private String titulo;
    private String descricao;

    private Boolean ativo = true;
    private LocalDateTime criadoEm = LocalDateTime.now();
}