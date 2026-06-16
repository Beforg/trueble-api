package unipampa.trueble.api.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import unipampa.trueble.api.enums.TipoQuestao;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Entity
@Table(name = "questoes")
public class Questao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_questao")
    private TipoQuestao tipoQuestao;

    private String categoria;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> conteudo;

    private Boolean ativo = true;
    private Boolean publico = false;
    private LocalDateTime criadoEm = LocalDateTime.now();
}