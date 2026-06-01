package unipampa.trueble.api.services;

import org.springframework.stereotype.Service;
import unipampa.trueble.api.domain.Aluno;
import unipampa.trueble.api.domain.Professor;
import unipampa.trueble.api.domain.Role;
import unipampa.trueble.api.domain.Usuario;
import unipampa.trueble.api.dto.RegistroUsuarioDTO;
import unipampa.trueble.api.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

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
}