package unipampa.trueble.api.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import unipampa.trueble.api.domain.Aluno;
import unipampa.trueble.api.domain.Professor;
import unipampa.trueble.api.dto.UsuarioResponseDTO;
import unipampa.trueble.api.dto.AvatarUploadResponseDTO;
import unipampa.trueble.api.dto.AvatarUpdateRequestDTO;
import unipampa.trueble.api.enums.Role;
import unipampa.trueble.api.domain.Usuario;
import unipampa.trueble.api.dto.RegistroUsuarioDTO;
import unipampa.trueble.api.repository.UsuarioRepository;

import java.util.UUID;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final SupabaseStorageService supabaseStorageService;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            SupabaseStorageService supabaseStorageService) {
        this.usuarioRepository = usuarioRepository;
        this.supabaseStorageService = supabaseStorageService;
    }

    @Transactional
    public Usuario registrarNovoUsuario(RegistroUsuarioDTO dto) {
        if (dto.role() == Role.PROFESSOR) {
            Professor professor = new Professor();
            professor.setId(dto.id());
            professor.setEmail(dto.email());
            professor.setNome(dto.nome());
            professor.setRole(Role.PROFESSOR);
            return usuarioRepository.save(professor);
        } else {
            Aluno aluno = new Aluno();
            aluno.setId(dto.id());
            aluno.setEmail(dto.email());
            aluno.setNome(dto.nome());
            aluno.setRole(Role.ALUNO);
            return usuarioRepository.save(aluno);
        }
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO obterPerfilAutenticado(UUID usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuário não encontrado."));

        return new UsuarioResponseDTO(usuario);
    }

    @Transactional(readOnly = true)
    public AvatarUploadResponseDTO criarUrlUploadAvatar(UUID usuarioId, String contentType) {
        buscarUsuario(usuarioId);
        return supabaseStorageService.criarUrlUpload(usuarioId, contentType);
    }

    @Transactional
    public UsuarioResponseDTO atualizarAvatar(UUID usuarioId, AvatarUpdateRequestDTO dto) {
        Usuario usuario = buscarUsuario(usuarioId);
        String expectedPrefix = "usuarios/" + usuarioId + "/";
        if (dto.path() == null || !dto.path().startsWith(expectedPrefix)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O caminho da imagem não pertence ao usuário autenticado.");
        }

        usuario.setAvatarPath(dto.path());
        return new UsuarioResponseDTO(usuarioRepository.save(usuario));
    }

    private Usuario buscarUsuario(UUID usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuário não encontrado."));
    }
}