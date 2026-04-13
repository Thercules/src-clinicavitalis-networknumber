package org.clinicavitalis.usuario.domain.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.clinicavitalis.shared.domain.vo.Email;
import org.clinicavitalis.usuario.domain.entity.NivelDeAcesso;
import org.clinicavitalis.usuario.domain.entity.Usuario;
import org.clinicavitalis.usuario.domain.exception.EmailAlreadyExistsException;
import org.clinicavitalis.usuario.domain.repository.UsuarioRepository;

@ApplicationScoped
public class UsuarioDomainService implements UsuarioService {

    @Inject
    UsuarioRepository usuarioRepository;

    public void validarEmailUnico(Email email) {
        if (usuarioRepository.existePorEmail(email)) {
            throw new EmailAlreadyExistsException(email.getValue());
        }
    }

    public Usuario registrarNovoUsuario(Usuario usuario) {
        validarEmailUnico(usuario.getEmail());
        return usuarioRepository.salvar(usuario);
    }

    public Usuario encontrarPorEmail(Email email) {
        return usuarioRepository
            .encontrarPorEmail(email)
            .orElseThrow(() ->
                new org.clinicavitalis.shared.domain.exception.EntityNotFoundException(
                    "Usuario", email.getValue()
                )
            );
    }

    public Usuario encontrarPorId(Long id) {
        return usuarioRepository
            .encontrarPorId(id)
            .orElseThrow(() ->
                new org.clinicavitalis.shared.domain.exception.EntityNotFoundException(
                    "Usuario", id
                )
            );
    }

    public Usuario registrarUsuarioComNivel(Usuario usuario) {
        validarEmailUnico(usuario.getEmail());
        return usuarioRepository.salvar(usuario);
    }

    public boolean validarHierarquiaDeAcesso(NivelDeAcesso nivelDoUsuarioLogado, NivelDeAcesso nivelDoUsuarioASerCriado) {
        return nivelDoUsuarioLogado.podeGerenciar(nivelDoUsuarioASerCriado);
    }
}
