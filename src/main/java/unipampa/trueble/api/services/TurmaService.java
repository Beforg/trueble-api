package unipampa.trueble.api.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unipampa.trueble.api.domain.Aluno;
import unipampa.trueble.api.domain.AlunoTurma;
import unipampa.trueble.api.domain.Professor;
import unipampa.trueble.api.domain.Turma;
import unipampa.trueble.api.dto.TurmaRequestDTO;
import unipampa.trueble.api.dto.TurmaResponseDTO;
import unipampa.trueble.api.repository.AlunoTurmaRepository;
import unipampa.trueble.api.repository.UsuarioRepository;
import unipampa.trueble.api.repository.TurmaRepository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TurmaService {

    private final TurmaRepository turmaRepository;
    private final UsuarioRepository usuarioRepository;
    private final AlunoTurmaRepository alunoTurmaRepository;

    public TurmaService(TurmaRepository turmaRepository,
                        UsuarioRepository usuarioRepository,
                        AlunoTurmaRepository alunoTurmaRepository) {
        this.turmaRepository = turmaRepository;
        this.usuarioRepository = usuarioRepository;
        this.alunoTurmaRepository = alunoTurmaRepository;
    }

    @Transactional
    public TurmaResponseDTO criarTurma(UUID professorId, TurmaRequestDTO dto) {
        validarDatas(dto);

        Professor professor = (Professor) usuarioRepository.findById(professorId)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado."));

        Turma turma = new Turma();
        turma.setProfessor(professor);
        turma.setNome(dto.nomeTurma());
        turma.setDescricao(dto.descricao());
        turma.setDataInicio(dto.dataInicio());
        turma.setDataFim(dto.dataFim());
        turma.setAtivo(true);
        turma.setCriadoEm(LocalDateTime.now());

        turma = turmaRepository.save(turma);
        return new TurmaResponseDTO(turma);
    }

    @Transactional
    public TurmaResponseDTO editarTurma(UUID professorId, UUID turmaId, TurmaRequestDTO dto) {
        validarDatas(dto);

        Turma turma = turmaRepository.findById(turmaId)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada."));

        if (!turma.getProfessor().getId().equals(professorId)) {
            throw new RuntimeException("Sem permissão para editar esta turma.");
        }

        turma.setNome(dto.nomeTurma());
        turma.setDescricao(dto.descricao());
        turma.setDataInicio(dto.dataInicio());
        turma.setDataFim(dto.dataFim());

        turma = turmaRepository.save(turma);
        return new TurmaResponseDTO(turma);
    }

    @Transactional(readOnly = true)
    public List<TurmaResponseDTO> listarMinhasTurmasComoProfessor(UUID professorId) {
        return turmaRepository.buscarTurmasDoProfessor(professorId)
                .stream()
                .map(TurmaResponseDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TurmaResponseDTO> listarMinhasTurmasComoAluno(UUID alunoId) {
        return turmaRepository.buscarTurmasDoAluno(alunoId)
                .stream()
                .map(TurmaResponseDTO::new)
                .toList();
    }

    @Transactional
    public void matricularAluno(UUID alunoId, UUID turmaId) {

        Turma turma = turmaRepository.findById(turmaId)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada."));

        if (!turma.getAtivo()) {
            throw new RuntimeException("Não é possível se matricular em uma turma inativa.");
        }

        if (alunoTurmaRepository.existsByAlunoIdAndTurmaId(alunoId, turmaId)) {
            throw new RuntimeException("Aluno já está matriculado nesta turma.");
        }


        Aluno aluno = alunoTurmaRepository.findById(alunoId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado.")).getAluno();

        AlunoTurma matricula = new AlunoTurma();
        matricula.setAluno(aluno);
        matricula.setTurma(turma);
        matricula.setDataEntrada(LocalDateTime.now()); // Opcional, pois a entidade já inicializa isso

        alunoTurmaRepository.save(matricula);
    }

    private void validarDatas(TurmaRequestDTO dto) {
        if (dto.dataInicio().isAfter(dto.dataFim())) {
            throw new IllegalArgumentException("A data de início não pode ser posterior à data de término.");
        }
    }
}