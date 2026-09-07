package unipampa.trueble.api.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unipampa.trueble.api.domain.ListaDeQuestoes;
import unipampa.trueble.api.domain.ListaQuestao;
import unipampa.trueble.api.domain.Professor;
import unipampa.trueble.api.domain.Questao;
import unipampa.trueble.api.domain.Turma;
import unipampa.trueble.api.dto.*;
import unipampa.trueble.api.repository.ListaDeQuestoesRepository;
import unipampa.trueble.api.repository.ListaQuestaoRepository;
import unipampa.trueble.api.repository.QuestaoRepository;
import unipampa.trueble.api.repository.TurmaRepository;
import unipampa.trueble.api.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ListaDeQuestoesService {

    private final ListaDeQuestoesRepository listaDeQuestoesRepository;
    private final ListaQuestaoRepository listaQuestaoRepository;
    private final QuestaoRepository questaoRepository;
    private final TurmaRepository turmaRepository;
    private final UsuarioRepository usuarioRepository;

    public ListaDeQuestoesService(ListaDeQuestoesRepository listaDeQuestoesRepository,
                                  ListaQuestaoRepository listaQuestaoRepository,
                                  QuestaoRepository questaoRepository,
                                  TurmaRepository turmaRepository,
                                  UsuarioRepository usuarioRepository) {
        this.listaDeQuestoesRepository = listaDeQuestoesRepository;
        this.listaQuestaoRepository = listaQuestaoRepository;
        this.questaoRepository = questaoRepository;
        this.turmaRepository = turmaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional 
    public ListaDeQuestoesResponseDTO criarLista(Jwt jwt, ListaDeQuestoesRequestDTO dto) {
        UUID professorId = UUID.fromString(jwt.getSubject());

        Professor professor = (Professor) usuarioRepository.findById(professorId)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        Turma turma = turmaRepository.findById(dto.turmaId())
                .orElseThrow(() -> new RuntimeException("Turma não encontrada"));

//        if (!turma.getProfessor().getId().equals(professorId)) {
//            throw new RuntimeException("Sem permissão para vincular esta lista a essa turma");
//        } DEsativado para testes

        // 1. Cria e guarda a "Capa" da Lista
        ListaDeQuestoes lista = new ListaDeQuestoes();
        lista.setProfessor(professor);
        lista.setTurma(turma);
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

    @Transactional(readOnly = true)
    public List<ListaDeQuestoesResponseDTO> listarMinhasListas(Jwt jwt) {
        UUID professorId = UUID.fromString(jwt.getSubject());

        List<ListaDeQuestoes> minhasListas = listaDeQuestoesRepository.findAllByProfessorIdAndAtivoTrue(professorId);

        return minhasListas.stream().map(lista -> {
            Integer totalQuestoes = listaQuestaoRepository.countByListaId(lista.getId());
            return new ListaDeQuestoesResponseDTO(lista, totalQuestoes);
        }).toList();
    }

    @Transactional(readOnly = true)
    public List<ListaDeQuestoesResponseDTO> listarListasDaTurma(UUID turmaId) {
        List<ListaDeQuestoes> listas = listaDeQuestoesRepository.findAllByTurmaIdAndAtivoTrue(turmaId);

        return listas.stream().map(lista -> {
        Integer totalQuestoes = listaQuestaoRepository.countByListaId(lista.getId());
        return new ListaDeQuestoesResponseDTO(lista, totalQuestoes);
        }).toList();
    }

    @Transactional
    public void adicionarQuestoes(Jwt jwt, UUID listaId, List<ListaQuestaoRequestDTO> novasQuestoes) {
        UUID professorId = UUID.fromString(jwt.getSubject()); // Ajuste conforme seu extrator

        ListaDeQuestoes lista = listaDeQuestoesRepository.findById(listaId)
                .orElseThrow(() -> new EntityNotFoundException("Lista não encontrada."));

        if (!lista.getProfessor().getId().equals(professorId)) {
            throw new AccessDeniedException("Você não tem permissão para alterar esta lista.");
        }

        // 4. Salva as novas vinculações
        for (ListaQuestaoRequestDTO dto : novasQuestoes) {
            Questao questao = questaoRepository.findById(dto.questaoId()) // Ajuste para o nome exato do campo no seu record
                    .orElseThrow(() -> new EntityNotFoundException("Questão não encontrada."));

            // Instancia a sua entidade associativa (provavelmente ListaQuestao)
            ListaQuestao vinculacao = new ListaQuestao();
            vinculacao.setLista(lista);
            vinculacao.setQuestao(questao);

            listaQuestaoRepository.save(vinculacao);
        }
    }

    @Transactional(readOnly = true)
    public List<ListaQuestaoResponseDTO> listarQuestoesDaLista(Jwt jwt, UUID listaId) {

        // 1. Verifica se a lista existe no banco de dados
        boolean listaExiste = listaDeQuestoesRepository.existsById(listaId);
        if (!listaExiste) {
            throw new EntityNotFoundException("Lista de exercícios não encontrada.");
        }

        // 2. Busca as associações (ListaQuestao) já ordenadas no banco
        List<ListaQuestao> listaQuestoes = listaQuestaoRepository.buscarComQuestoesPorListaId(listaId);

        // 3. Mapeia a entidade para o DTO de resposta
        return listaQuestoes.stream()
                .map(lq -> new ListaQuestaoResponseDTO(
                        lq.getQuestao().getId(),
                        lq.getOrdem(),
                        new QuestaoResponseDTO(lq.getQuestao()) // Adapte aqui caso seu QuestaoResponseDTO use um builder ou mapper
                ))
                .toList();
    }
}