package unipampa.trueble.api.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "alunos_turmas")
@AllArgsConstructor
@NoArgsConstructor
public class AlunoTurma {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "turma_id", nullable = false)
    private Turma turma;

    @Column(name = "data_entrada")
    private LocalDateTime dataEntrada = LocalDateTime.now();
}