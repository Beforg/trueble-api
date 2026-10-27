package unipampa.trueble.api.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import unipampa.trueble.api.domain.Categoria;
import unipampa.trueble.api.domain.Professor;
import unipampa.trueble.api.domain.Questao;
import unipampa.trueble.api.dto.QuestaoRequestDTO;
import unipampa.trueble.api.dto.QuestaoResponseDTO;
import unipampa.trueble.api.repository.CategoriaRepository;
import unipampa.trueble.api.repository.QuestaoRepository;
import unipampa.trueble.api.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class QuestaoService {

    private final QuestaoRepository questaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;

    public QuestaoService(
            QuestaoRepository questaoRepository,
            UsuarioRepository usuarioRepository,
            CategoriaRepository categoriaRepository) {
        this.questaoRepository = questaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional
    public QuestaoResponseDTO criarQuestao(Jwt jwt, QuestaoRequestDTO dto) {
        UUID professorId = UUID.fromString(jwt.getSubject());
        Professor professor = (Professor) usuarioRepository.findById(professorId)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        System.out.println(dto.categoria());
        Categoria categoria = categoriaRepository.findById(UUID.fromString(dto.categoria()))
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
        Questao questao = new Questao();
        questao.setProfessor(professor);
        questao.setTitulo(dto.titulo());
        questao.setDescricao(dto.descricao());
        questao.setTipoQuestao(dto.tipoQuestao());
        questao.setCategoria(categoria);
        questao.setConteudo(dto.conteudo());
        questao.setPublico(dto.publico() != null ? dto.publico() : false);
        questao.setAtivo(true);
        questao.setCriadoEm(LocalDateTime.now());

        questao = questaoRepository.save(questao);
        return new QuestaoResponseDTO(questao);
    }

    @Transactional
    public QuestaoResponseDTO atualizarQuestao(Jwt jwt, UUID questaoId, QuestaoRequestDTO dto) {
        UUID professorId = UUID.fromString(jwt.getSubject());
        Questao questao = buscarQuestaoDoProfessor(questaoId, professorId);

        questao.setTitulo(dto.titulo());
        questao.setDescricao(dto.descricao());
        questao.setConteudo(dto.conteudo());
        questao.setPublico(dto.publico() != null ? dto.publico() : false);

        return new QuestaoResponseDTO(questaoRepository.save(questao));
    }

    @Transactional
    public void deletarQuestao(Jwt jwt, UUID questaoId) {
        UUID professorId = UUID.fromString(jwt.getSubject());
        Questao questao = buscarQuestaoDoProfessor(questaoId, professorId);
        questao.setAtivo(false);
        questaoRepository.save(questao);
    }

    private Questao buscarQuestaoDoProfessor(UUID questaoId, UUID professorId) {
        Questao questao = questaoRepository.findById(questaoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Questão não encontrada."));

        if (!questao.getProfessor().getId().equals(professorId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Sem permissão para alterar esta questão.");
        }
        if (!Boolean.TRUE.equals(questao.getAtivo())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Questão não encontrada.");
        }
        return questao;
    }

    @Transactional(readOnly = true)
    public List<QuestaoResponseDTO> listarMinhasQuestoes(Jwt jwt) {
        UUID professorId = UUID.fromString(jwt.getSubject());
        return questaoRepository.findAllByProfessorIdAndAtivoTrue(professorId)
                .stream()
                .map(QuestaoResponseDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<QuestaoResponseDTO> listarQuestoesPublicas() {
        return questaoRepository.findAllByPublicoTrueAndAtivoTrue()
                .stream()
                .map(QuestaoResponseDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<QuestaoResponseDTO> listarBancoDeQuestoes(
            Jwt jwt,
            UUID categoriaId,
            Pageable pageable) {

        UUID professorId = UUID.fromString(jwt.getSubject());

        Page<Questao> questoes = questaoRepository.buscarBancoDeQuestoes(
                 categoriaId, professorId, pageable
        );
        return questoes.map(QuestaoResponseDTO::new);
    }
}