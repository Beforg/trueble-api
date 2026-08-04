package unipampa.trueble.api.services;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import unipampa.trueble.api.domain.Professor;
import unipampa.trueble.api.domain.Questao;
import unipampa.trueble.api.dto.QuestaoRequestDTO;
import unipampa.trueble.api.dto.QuestaoResponseDTO;
import unipampa.trueble.api.repository.QuestaoRepository;
import unipampa.trueble.api.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class QuestaoService {

    private final QuestaoRepository questaoRepository;
    private final UsuarioRepository usuarioRepository;

    public QuestaoService(QuestaoRepository questaoRepository, UsuarioRepository usuarioRepository) {
        this.questaoRepository = questaoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public QuestaoResponseDTO criarQuestao(Jwt jwt, QuestaoRequestDTO dto) {
        UUID professorId = UUID.fromString(jwt.getSubject());
        System.out.println(UUID.fromString(jwt.getSubject()));
        Professor professor = (Professor) usuarioRepository.findById(professorId)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        Questao questao = new Questao();
        questao.setProfessor(professor);
        questao.setTitulo(dto.titulo());
        questao.setDescricao(dto.descricao());
        questao.setTipoQuestao(dto.tipoQuestao());
        questao.setCategoria(dto.categoria());
        questao.setConteudo(dto.conteudo());
        questao.setPublico(dto.publico() != null ? dto.publico() : false);
        questao.setAtivo(true);
        questao.setCriadoEm(LocalDateTime.now());

        questao = questaoRepository.save(questao);
        return new QuestaoResponseDTO(questao);
    }

    public List<QuestaoResponseDTO> listarMinhasQuestoes(Jwt jwt) {
        UUID professorId = UUID.fromString(jwt.getSubject());
        return questaoRepository.findAllByProfessorIdAndAtivoTrue(professorId)
                .stream()
                .map(QuestaoResponseDTO::new)
                .toList();
    }

    public List<QuestaoResponseDTO> listarQuestoesPublicas() {
        return questaoRepository.findAllByPublicoTrueAndAtivoTrue()
                .stream()
                .map(QuestaoResponseDTO::new)
                .toList();
    }
}