package unipampa.trueble.api.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unipampa.trueble.api.domain.Categoria;
import unipampa.trueble.api.domain.Professor;
import unipampa.trueble.api.domain.Questao;
import unipampa.trueble.api.dto.QuestaoRequestDTO;
import unipampa.trueble.api.dto.QuestaoResponseDTO;
import unipampa.trueble.api.enums.TipoQuestao;
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
        System.out.println(UUID.fromString(jwt.getSubject()));
        Professor professor = (Professor) usuarioRepository.findById(professorId)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));
        Categoria categoria = categoriaRepository.findByNome(dto.categoria())
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
            String titulo,
            TipoQuestao tipoQuestao,
            UUID categoriaId,
            Pageable pageable) {

        UUID professorId = UUID.fromString(jwt.getSubject());

        Page<Questao> questoes = questaoRepository.buscarBancoDeQuestoes(
                titulo, tipoQuestao, categoriaId, professorId, pageable
        );
        return questoes.map(QuestaoResponseDTO::new);
    }
}