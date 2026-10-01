package unipampa.trueble.api.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "atividades")
@AllArgsConstructor
@NoArgsConstructor
public class Atividade {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "turma_id", nullable = false)
    private Turma turma;

    @ManyToOne
    @JoinColumn(name = "lista_id", nullable = false)
    private ListaDeQuestoes lista;

    private String titulo;

    @Column(name = "data_liberacao")
    private LocalDateTime dataLiberacao;

    @Column(name = "data_entrega")
    private LocalDateTime dataEntrega;

    private LocalDateTime criadaEm = LocalDateTime.now();
}
