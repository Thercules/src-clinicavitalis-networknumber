package org.clinicavitalis.usuario.application.service;

import org.clinicavitalis.usuario.application.dto.AuthTokenResponse;
import org.clinicavitalis.usuario.application.dto.CreateUserRequest;
import org.clinicavitalis.usuario.application.dto.LoginRequest;
import org.clinicavitalis.usuario.application.dto.RegisterRequest;
import org.clinicavitalis.usuario.application.dto.UsuarioResponse;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@ApplicationScoped
public class AutenticacaoApplicationService {

    @Inject
    RegistrarUsuarioUseCase registrarUseCase;

    @Inject
    LoginUseCase loginUseCase;

    @Inject
    RenovarTokenUseCase renovarTokenUseCase;

    @Inject
    CriarUsuarioComNivelUseCase criarUsuarioComNivelUseCase;

    public AuthTokenResponse registrar(@Valid @NotNull RegisterRequest request) {
        return registrarUseCase.executar(request);
    }

    public AuthTokenResponse login(@Valid @NotNull LoginRequest request) {
        return loginUseCase.executar(request);
    }

    public String renovarToken(String refreshToken) {
        return renovarTokenUseCase.executar(refreshToken);
    }

    public UsuarioResponse criarUsuarioComNivel(
        @Valid @NotNull CreateUserRequest request,
        Long usuarioAutenticadoId,
        String nivelDoUsuarioAutenticado
    ) {
        return criarUsuarioComNivelUseCase.executar(request, usuarioAutenticadoId, nivelDoUsuarioAutenticado);
    }
}
