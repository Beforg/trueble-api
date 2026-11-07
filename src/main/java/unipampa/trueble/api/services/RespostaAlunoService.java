package unipampa.trueble.api.services;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unipampa.trueble.api.domain.Aluno;
import unipampa.trueble.api.domain.ListaDeQuestoes;
import unipampa.trueble.api.domain.Questao;
import unipampa.trueble.api.domain.RespostaAluno;
import unipampa.trueble.api.dto.RespostaAlunoRequestDTO;
import unipampa.trueble.api.dto.RespostaAlunoResponseDTO;
import unipampa.trueble.api.repository.AlunoTurmaRepository;
import unipampa.trueble.api.repository.ListaDeQuestoesRepository;
import unipampa.trueble.api.repository.ListaQuestaoRepository;
import unipampa.trueble.api.repository.QuestaoRepository;
import unipampa.trueble.api.repository.RespostaAlunoRepository;
import unipampa.trueble.api.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RespostaAlunoService {

    private final RespostaAlunoRepository respostaAlunoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ListaDeQuestoesRepository listaDeQuestoesRepository;
    private final ListaQuestaoRepository listaQuestaoRepository;
    private final QuestaoRepository questaoRepository;
    private final AlunoTurmaRepository alunoTurmaRepository;

    public RespostaAlunoService(RespostaAlunoRepository respostaAlunoRepository,
                                UsuarioRepository usuarioRepository,
                                ListaDeQuestoesRepository listaDeQuestoesRepository,
                                ListaQuestaoRepository listaQuestaoRepository,
                                QuestaoRepository questaoRepository,
                                AlunoTurmaRepository alunoTurmaRepository) {
        this.respostaAlunoRepository = respostaAlunoRepository;
        this.usuarioRepository = usuarioRepository;
        this.listaDeQuestoesRepository = listaDeQuestoesRepository;
        this.listaQuestaoRepository = listaQuestaoRepository;
        this.questaoRepository = questaoRepository;
        this.alunoTurmaRepository = alunoTurmaRepository;
    }

    @Transactional
    public RespostaAlunoResponseDTO criarResposta(Jwt jwt, UUID listaId, RespostaAlunoRequestDTO dto) {
        UUID alunoId = UUID.fromString(jwt.getSubject());
        var usuario = usuarioRepository.findById(alunoId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        if (!(usuario instanceof Aluno aluno)) {
            throw new RuntimeException("Usuário autenticado não é um aluno");
        }
        ListaDeQuestoes lista = listaDeQuestoesRepository.findById(listaId)
                .orElseThrow(() -> new RuntimeException("Lista de questões não encontrada"));

        if (!alunoTurmaRepository.existsByAlunoIdAndTurmaId(alunoId, lista.getTurma().getId())) {
            throw new RuntimeException("Aluno não pertence à turma da lista");
        }
        if (!listaQuestaoRepository.existsByListaIdAndQuestaoId(listaId, dto.questaoId())) {
            throw new RuntimeException("Questão não pertence à lista");
        }

        Questao questao = questaoRepository.findById(dto.questaoId())
                .orElseThrow(() -> new RuntimeException("Questão não encontrada"));

        RespostaAluno respostaAluno = new RespostaAluno();
        respostaAluno.setAluno(aluno);
        respostaAluno.setLista(lista);
        respostaAluno.setQuestao(questao);
        respostaAluno.setResposta(dto.resposta());
        respostaAluno.setRespondidaEm(LocalDateTime.now());

        return new RespostaAlunoResponseDTO(respostaAlunoRepository.save(respostaAluno));
    }
}
