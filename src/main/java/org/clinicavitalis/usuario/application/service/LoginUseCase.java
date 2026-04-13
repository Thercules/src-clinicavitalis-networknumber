package org.clinicavitalis.usuario.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.clinicavitalis.shared.domain.vo.Email;
import org.clinicavitalis.usuario.application.dto.AuthTokenResponse;
import org.clinicavitalis.usuario.application.dto.LoginRequest;
import org.clinicavitalis.usuario.application.dto.UsuarioResponse;
import org.clinicavitalis.usuario.domain.entity.Usuario;
import org.clinicavitalis.usuario.domain.port.PasswordHashingPort;
import org.clinicavitalis.usuario.domain.port.TokenProvider;
import org.clinicavitalis.usuario.domain.repository.UsuarioRepository;
import org.clinicavitalis.usuario.domain.service.UsuarioService;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;

@ApplicationScoped
public class LoginUseCase {

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    UsuarioService usuarioDomainService;

    @Inject
    TokenProvider tokenProvider;

    @Inject
    PasswordHashingPort passwordHasher;

    @Transactional
    @CircuitBreaker(
        requestVolumeThreshold = 4,
        failureRatio = 0.5,
        delay = 5000,
        skipOn = {
            org.clinicavitalis.usuario.domain.exception.CredenciaisInvalidasException.class,
            org.clinicavitalis.usuario.domain.exception.UsuarioDomainException.class,
            org.clinicavitalis.shared.domain.exception.EntityNotFoundException.class
        }
    )
    public AuthTokenResponse executar(@Valid @NotNull LoginRequest request) {
        Email email = Email.of(request.getEmail());
        Usuario usuario = usuarioDomainService.encontrarPorEmail(email);

        if (!usuario.getAtivo())
            throw new org.clinicavitalis.usuario.domain.exception.UsuarioDomainException("Usuário inativo");

        usuario.verificarSenha(request.getPassword(), passwordHasher::verify);
        usuario.marcarUltimoAcesso();
        usuario = usuarioRepository.atualizar(usuario);

        String token = tokenProvider.generateToken(usuario);
        String refreshToken = tokenProvider.generateRefreshToken(usuario);
        UsuarioResponse usuarioResponse = mapToUsuarioResponse(usuario);

        return new AuthTokenResponse(token, refreshToken, usuarioResponse);
    }

    private UsuarioResponse mapToUsuarioResponse(Usuario usuario) {
        return new UsuarioResponse(
            usuario.getId(),
            usuario.getEmail().getValue(),
            usuario.getNomeCompleto(),
            usuario.getNivelDeAcesso().getCodigo(),
            usuario.getEmailVerificado()
        );
    }
}
