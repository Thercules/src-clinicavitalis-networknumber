package org.clinicavitalis.usuario.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.clinicavitalis.shared.domain.vo.Email;
import org.clinicavitalis.shared.domain.vo.Telefone;
import org.clinicavitalis.usuario.application.dto.AuthTokenResponse;
import org.clinicavitalis.usuario.application.dto.RegisterRequest;
import org.clinicavitalis.usuario.application.dto.UsuarioResponse;
import org.clinicavitalis.usuario.domain.entity.Usuario;
import org.clinicavitalis.usuario.domain.port.PasswordHashingPort;
import org.clinicavitalis.usuario.domain.port.TokenProvider;
import org.clinicavitalis.usuario.domain.service.UsuarioService;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;

@ApplicationScoped
public class RegistrarUsuarioUseCase {

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
            org.clinicavitalis.usuario.domain.exception.EmailAlreadyExistsException.class,
            IllegalArgumentException.class
        }
    )
    public AuthTokenResponse executar(@Valid @NotNull RegisterRequest request) {
        validarSenhas(request.getPassword(), request.getConfirmPassword());
        validarForcaSenha(request.getPassword());

        Email email = Email.of(request.getEmail());
        Telefone telefone = parseTelefone(request.getTelefone());
        String senhaHasheada = passwordHasher.hash(request.getPassword());

        Usuario usuario = Usuario.criar(email, senhaHasheada, request.getNome_completo(), telefone, null);
        usuario = usuarioDomainService.registrarNovoUsuario(usuario);

        String token = tokenProvider.generateToken(usuario);
        String refreshToken = tokenProvider.generateRefreshToken(usuario);
        UsuarioResponse usuarioResponse = mapToUsuarioResponse(usuario);

        return new AuthTokenResponse(token, refreshToken, usuarioResponse);
    }

    private Telefone parseTelefone(String telefone) {
        if (telefone == null || telefone.isEmpty()) return null;
        try { return Telefone.of(telefone); } catch (IllegalArgumentException e) { return null; }
    }

    private void validarSenhas(String password, String confirmPassword) {
        if (!password.equals(confirmPassword))
            throw new IllegalArgumentException("Senhas não coincidem");
    }

    private void validarForcaSenha(String senha) {
        if (senha == null || senha.length() < 8)
            throw new IllegalArgumentException("Senha deve ter no mínimo 8 caracteres");
        if (!senha.matches(".*[A-Z].*"))
            throw new IllegalArgumentException("Senha deve conter pelo menos uma letra maiúscula");
        if (!senha.matches(".*[a-z].*"))
            throw new IllegalArgumentException("Senha deve conter pelo menos uma letra minúscula");
        if (!senha.matches(".*\\d.*"))
            throw new IllegalArgumentException("Senha deve conter pelo menos um número");
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
