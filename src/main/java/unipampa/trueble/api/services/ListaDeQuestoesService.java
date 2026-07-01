package unipampa.trueble.api.services;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unipampa.trueble.api.domain.ListaDeQuestoes;
import unipampa.trueble.api.domain.ListaQuestao;
import unipampa.trueble.api.domain.Professor;
import unipampa.trueble.api.domain.Questao;
import unipampa.trueble.api.dto.ListaDeQuestoesRequestDTO;
import unipampa.trueble.api.dto.ListaDeQuestoesResponseDTO;
import unipampa.trueble.api.dto.ListaQuestaoRequestDTO;
import unipampa.trueble.api.repository.ListaDeQuestoesRepository;
import unipampa.trueble.api.repository.ListaQuestaoRepository;
import unipampa.trueble.api.repository.QuestaoRepository;
import unipampa.trueble.api.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ListaDeQuestoesService {

    private final ListaDeQuestoesRepository listaDeQuestoesRepository;
    private final ListaQuestaoRepository listaQuestaoRepository;
    private final QuestaoRepository questaoRepository;
    private final UsuarioRepository usuarioRepository;

    public ListaDeQuestoesService(ListaDeQuestoesRepository listaDeQuestoesRepository,
                                  ListaQuestaoRepository listaQuestaoRepository,
                                  QuestaoRepository questaoRepository,
                                  UsuarioRepository usuarioRepository) {
        this.listaDeQuestoesRepository = listaDeQuestoesRepository;
        this.listaQuestaoRepository = listaQuestaoRepository;
        this.questaoRepository = questaoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional 
    public ListaDeQuestoesResponseDTO criarLista(Jwt jwt, ListaDeQuestoesRequestDTO dto) {
        UUID professorId = UUID.fromString(jwt.getSubject());

        Professor professor = (Professor) usuarioRepository.findById(professorId)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        // 1. Cria e guarda a "Capa" da Lista
        ListaDeQuestoes lista = new ListaDeQuestoes();
        lista.setProfessor(professor);
        lista.setTitulo(dto.titulo());
        lista.setDescricao(dto.descricao());
        lista.setAtivo(true);
        lista.setCriadoEm(LocalDateTime.now());
        lista = listaDeQuestoesRepository.save(lista);

        // 2. Guarda a Entidade "Ponte" (ListaQuestao) para associar as questões à lista
        if (dto.questoes() != null && !dto.questoes().isEmpty()) {
            for (ListaQuestaoRequestDTO questaoDto : dto.questoes()) {
                Questao questao = questaoRepository.findById(questaoDto.questaoId())
                        .orElseThrow(() -> new RuntimeException("Questão não encontrada: " + questaoDto.questaoId()));

                ListaQuestao listaQuestao = new ListaQuestao();
                listaQuestao.setLista(lista);
                listaQuestao.setQuestao(questao);
                listaQuestao.setOrdem(questaoDto.ordem());

                listaQuestaoRepository.save(listaQuestao);
            }
        }

        int totalQuestoes = dto.questoes() != null ? dto.questoes().size() : 0;
        return new ListaDeQuestoesResponseDTO(lista, totalQuestoes);
    }

    public List<ListaDeQuestoesResponseDTO> listarMinhasListas(Jwt jwt) {
        UUID professorId = UUID.fromString(jwt.getSubject());

        List<ListaDeQuestoes> minhasListas = listaDeQuestoesRepository.findAllByProfessorIdAndAtivoTrue(professorId);

        return minhasListas.stream().map(lista -> {
            Integer totalQuestoes = listaQuestaoRepository.countByListaId(lista.getId());
            return new ListaDeQuestoesResponseDTO(lista, totalQuestoes);
        }).toList();
    }
}