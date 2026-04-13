package org.clinicavitalis.usuario.domain.service;

import org.clinicavitalis.shared.domain.vo.Email;
import org.clinicavitalis.usuario.domain.entity.NivelDeAcesso;
import org.clinicavitalis.usuario.domain.entity.Usuario;

public interface UsuarioService {

    void validarEmailUnico(Email email);

    Usuario registrarNovoUsuario(Usuario usuario);

    Usuario encontrarPorEmail(Email email);

    Usuario encontrarPorId(Long id);

    Usuario registrarUsuarioComNivel(Usuario usuario);

    boolean validarHierarquiaDeAcesso(NivelDeAcesso nivelDoUsuarioLogado, NivelDeAcesso nivelDoUsuarioASerCriado);
}
