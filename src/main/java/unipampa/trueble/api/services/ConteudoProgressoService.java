package unipampa.trueble.api.services;

import jakarta.transaction.Transactional;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import unipampa.trueble.api.domain.Aluno;
import unipampa.trueble.api.domain.ConteudoProgresso;
import unipampa.trueble.api.dto.ProgressoDTO;
import unipampa.trueble.api.enums.StatusProgresso;
import unipampa.trueble.api.repository.ConteudoProgressoRepository;
import unipampa.trueble.api.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ConteudoProgressoService {
    private final ConteudoProgressoRepository conteudoProgressoRepository;
    private final UsuarioRepository usuarioRepository;

    private ConteudoProgressoService(ConteudoProgressoRepository conteudoProgressoRepository, UsuarioRepository usuarioRepository) {
        this.conteudoProgressoRepository = conteudoProgressoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<ProgressoDTO> listarMeusProgressos(Jwt jwt) {
        UUID alunoId = UUID.fromString(jwt.getSubject()); // Pega o ID do Supabase

        return conteudoProgressoRepository.findAllByAlunoId(alunoId).stream().map(ProgressoDTO::new).toList();
    }

    @Transactional
    public ProgressoDTO atualizarProgresso(Jwt jwt, ProgressoDTO dto) {
        UUID alunoId = UUID.fromString(jwt.getSubject());

        // Procura se o aluno já começou este conteúdo
        ConteudoProgresso progresso = conteudoProgressoRepository
                .findByAlunoIdAndConteudoId(alunoId, UUID.fromString(dto.conteudoId()))
                .orElse(new ConteudoProgresso());// Se não achar, cria um novo!

        if (progresso.getId() == null) {
            // É a primeira vez (startContent)
            Aluno aluno = (Aluno) usuarioRepository.findById(alunoId)
                    .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
            progresso.setAluno(aluno);
            progresso.setConteudoId(dto.conteudoId());
        }

        // Atualiza os dados
        progresso.setPaginasLidas(dto.paginasLidas());
        progresso.setTotalPaginas(dto.totalPaginas());
        progresso.setStatus(StatusProgresso.valueOf(dto.status()));
        progresso.setAtualizadoEm(LocalDateTime.now());

        conteudoProgressoRepository.save(progresso);
        return new ProgressoDTO(progresso);
    }
}
