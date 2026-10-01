package unipampa.trueble.api.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Table(name = "lista_questao")
@AllArgsConstructor
@NoArgsConstructor
public class ListaQuestao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "lista_id", nullable = false)
    private ListaDeQuestoes lista;

    @ManyToOne
    @JoinColumn(name = "questao_id", nullable = false)
    private Questao questao;

    private Integer ordem;
}