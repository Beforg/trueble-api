package unipampa.trueble.api.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Entity
@Table(name = "respostas_alunos")
public class RespostaAluno {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "atividade_id", nullable = false)
    private Atividade atividade;

    @ManyToOne
    @JoinColumn(name = "questao_id", nullable = false)
    private Questao questao;

    // Resposta flexível gravada em JSONB!
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> resposta;

    private Boolean correta;
    private Double nota;

    @Column(name = "respondida_em")
    private LocalDateTime respondidaEm = LocalDateTime.now();
}