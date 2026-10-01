package unipampa.trueble.api.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "listas_de_questoes")
@AllArgsConstructor
@NoArgsConstructor
public class ListaDeQuestoes {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    @ManyToOne
    @JoinColumn(name = "turma_id", nullable = false)
    private Turma turma;

    private String titulo;
    private String descricao;

    private Boolean ativo = true;
    private LocalDateTime criadoEm = LocalDateTime.now();
    @Column(name = "data_inicio")
    private LocalDate dataInicio;
    @Column(name = "data_fim")
    private LocalDate dataFim;
}