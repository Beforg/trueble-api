package unipampa.trueble.api.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "alunos")
@Getter
@Setter
public class Aluno extends Usuario {

    @Column(nullable = false)
    private String nome;

}
