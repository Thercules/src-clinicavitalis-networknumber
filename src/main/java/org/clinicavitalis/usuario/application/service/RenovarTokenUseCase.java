package org.clinicavitalis.usuario.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.clinicavitalis.usuario.domain.entity.Usuario;
import org.clinicavitalis.usuario.domain.port.TokenProvider;
import org.clinicavitalis.usuario.domain.service.UsuarioService;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;

@ApplicationScoped
public class RenovarTokenUseCase {

    @Inject
    UsuarioService usuarioDomainService;

    @Inject
    TokenProvider tokenProvider;

    @CircuitBreaker(
        requestVolumeThreshold = 4,
        failureRatio = 0.5,
        delay = 5000,
        skipOn = { org.clinicavitalis.usuario.domain.exception.UsuarioDomainException.class }
    )
    public String executar(String refreshToken) {
        if (!tokenProvider.isTokenValid(refreshToken))
            throw new org.clinicavitalis.usuario.domain.exception.UsuarioDomainException(
                "Refresh token inválido ou expirado");

        if (!tokenProvider.isRefreshToken(refreshToken))
            throw new org.clinicavitalis.usuario.domain.exception.UsuarioDomainException(
                "Token não é um refresh token válido");

        Long usuarioId = tokenProvider.getIdFromToken(refreshToken);
        Usuario usuario = usuarioDomainService.encontrarPorId(usuarioId);

        return tokenProvider.generateToken(usuario);
    }
}
