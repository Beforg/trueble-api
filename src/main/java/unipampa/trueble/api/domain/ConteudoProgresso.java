package unipampa.trueble.api.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import unipampa.trueble.api.enums.StatusProgresso;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(
        name = "conteudo_progresso_aluno",
        uniqueConstraints = @UniqueConstraint(columnNames = {"aluno_id", "conteudo_id"})
)
@AllArgsConstructor
@NoArgsConstructor
public class ConteudoProgresso {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @Column(name = "conteudo_id", nullable = false)
    private Long conteudoId;

    @Column(name = "paginas_lidas")
    private Integer paginasLidas = 0;

    @Column(name = "total_paginas")
    private Integer totalPaginas = 0;

    @Enumerated(EnumType.STRING)
    private StatusProgresso status;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm = LocalDateTime.now();
}
