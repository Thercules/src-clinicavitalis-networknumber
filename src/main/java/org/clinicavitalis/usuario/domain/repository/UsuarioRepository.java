package org.clinicavitalis.usuario.domain.repository;

import org.clinicavitalis.usuario.domain.entity.Usuario;
import org.clinicavitalis.shared.domain.vo.Email;

import java.util.Optional;

public interface UsuarioRepository {

    Usuario salvar(Usuario usuario);

    Optional<Usuario> encontrarPorEmail(Email email);

    Optional<Usuario> encontrarPorId(Long id);

    boolean existePorEmail(Email email);

    Usuario atualizar(Usuario usuario);
}
