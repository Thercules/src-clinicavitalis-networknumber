package org.clinicavitalis.usuario.application.service;

import org.clinicavitalis.shared.domain.vo.CPF;
import org.clinicavitalis.shared.domain.vo.Email;
import org.clinicavitalis.shared.domain.vo.Telefone;
import org.clinicavitalis.usuario.application.dto.AuthTokenResponse;
import org.clinicavitalis.usuario.application.dto.CreateUserRequest;
import org.clinicavitalis.usuario.application.dto.LoginRequest;
import org.clinicavitalis.usuario.application.dto.RegisterRequest;
import org.clinicavitalis.usuario.application.dto.UsuarioResponse;
import org.clinicavitalis.usuario.domain.entity.NivelDeAcesso;
import org.clinicavitalis.usuario.domain.entity.Usuario;
import org.clinicavitalis.usuario.domain.repository.UsuarioRepository;
import org.clinicavitalis.usuario.domain.service.UsuarioDomainService;
import org.clinicavitalis.usuario.infrastructure.security.JwtTokenProvider;
import org.clinicavitalis.usuario.infrastructure.security.PasswordHasher;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;

@ApplicationScoped
public class AutenticacaoApplicationService {

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    UsuarioDomainService usuarioDomainService;

    @Inject
    JwtTokenProvider jwtTokenProvider;

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
    public AuthTokenResponse registrar(@Valid @NotNull RegisterRequest request) {

        validarRegistro(request);

        Email email = Email.of(request.getEmail());

        Telefone telefone = null;
        if (request.getTelefone() != null && !request.getTelefone().isEmpty()) {
            try {
                telefone = Telefone.of(request.getTelefone());
            } catch (IllegalArgumentException e) {

            }
        }

        String senhaHasheada = PasswordHasher.hash(request.getPassword());

        Usuario usuario = Usuario.criar(
            email,
            senhaHasheada,
            request.getNome_completo(),
            telefone,
            null
        );

        usuario = usuarioDomainService.registrarNovoUsuario(usuario);

        String token = jwtTokenProvider.generateToken(usuario);
        String refreshToken = jwtTokenProvider.generateRefreshToken(usuario);

        UsuarioResponse usuarioResponse = mapToUsuarioResponse(usuario);
        AuthTokenResponse response = new AuthTokenResponse(token, refreshToken, usuarioResponse);

        return response;
    }

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
    public AuthTokenResponse login(@Valid @NotNull LoginRequest request) {

        Email email = Email.of(request.getEmail());
        Usuario usuario = usuarioDomainService.encontrarPorEmail(email);

        if (!usuario.getAtivo()) {
            throw new org.clinicavitalis.usuario.domain.exception.UsuarioDomainException(
                "Usuário inativo"
            );
        }

        usuario.verificarSenha(
            request.getPassword(),
            PasswordHasher::verify
        );

        usuario.marcarUltimoAcesso();
        usuario = usuarioRepository.atualizar(usuario);

        String token = jwtTokenProvider.generateToken(usuario);
        String refreshToken = jwtTokenProvider.generateRefreshToken(usuario);

        UsuarioResponse usuarioResponse = mapToUsuarioResponse(usuario);
        AuthTokenResponse response = new AuthTokenResponse(token, refreshToken, usuarioResponse);

        return response;
    }

    @CircuitBreaker(
        requestVolumeThreshold = 4,
        failureRatio = 0.5,
        delay = 5000,
        skipOn = { org.clinicavitalis.usuario.domain.exception.UsuarioDomainException.class }
    )
    public String renovarToken(String refreshToken) {

        if (!jwtTokenProvider.isTokenValid(refreshToken)) {
            throw new org.clinicavitalis.usuario.domain.exception.UsuarioDomainException(
                "Refresh token inválido ou expirado"
            );
        }

        if (!jwtTokenProvider.isRefreshToken(refreshToken)) {
            throw new org.clinicavitalis.usuario.domain.exception.UsuarioDomainException(
                "Token não é um refresh token válido"
            );
        }

        Long usuarioId = jwtTokenProvider.getIdFromToken(refreshToken);
        Usuario usuario = usuarioDomainService.encontrarPorId(usuarioId);

        return jwtTokenProvider.generateToken(usuario);
    }

    @Transactional
    @CircuitBreaker(
        requestVolumeThreshold = 4,
        failureRatio = 0.5,
        delay = 5000,
        skipOn = {
            org.clinicavitalis.usuario.domain.exception.EmailAlreadyExistsException.class,
            org.clinicavitalis.usuario.domain.exception.UsuarioDomainException.class,
            IllegalArgumentException.class
        }
    )
    public UsuarioResponse criarUsuarioComNivel(
        @Valid @NotNull CreateUserRequest request,
        Long usuarioAutenticadoId,
        String nivelDoUsuarioAutenticado
    ) {

        validarCriacaoDeUsuario(request);

        NivelDeAcesso nivelAutenticado = NivelDeAcesso.fromCodigo(nivelDoUsuarioAutenticado);
        if (!usuarioDomainService.validarHierarquiaDeAcesso(nivelAutenticado, nivelAutenticado)) {
            throw new org.clinicavitalis.usuario.domain.exception.UsuarioDomainException(
                "Apenas usuários GM podem criar outros usuários"
            );
        }

        Email email = Email.of(request.getEmail());

        Telefone telefone = null;
        if (request.getTelefone() != null && !request.getTelefone().isEmpty()) {
            try {
                telefone = Telefone.of(request.getTelefone());
            } catch (IllegalArgumentException e) {

            }
        }

        CPF cpf = null;
        if (request.getCpf() != null && !request.getCpf().isEmpty()) {
            try {
                cpf = CPF.of(request.getCpf());
            } catch (IllegalArgumentException e) {

            }
        }

        String senhaHasheada = PasswordHasher.hash(request.getPassword());

        NivelDeAcesso nivelDeAcesso = NivelDeAcesso.fromCodigo(request.getNivel_de_acesso());

        Usuario usuario = Usuario.criarComNivel(
            email,
            senhaHasheada,
            request.getNome_completo(),
            telefone,
            cpf,
            nivelDeAcesso,
            usuarioAutenticadoId
        );

        usuario = usuarioDomainService.registrarUsuarioComNivel(usuario);

        return mapToUsuarioResponse(usuario);
    }

    private void validarRegistro(RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Senhas não coincidem");
        }

        validarForcaSenha(request.getPassword());
    }

    private void validarCriacaoDeUsuario(CreateUserRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Senhas não coincidem");
        }

        validarForcaSenha(request.getPassword());

        try {
            NivelDeAcesso.fromCodigo(request.getNivel_de_acesso());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Nível de acesso inválido: " + request.getNivel_de_acesso());
        }
    }

    private void validarForcaSenha(String senha) {
        if (senha == null || senha.length() < 8) {
            throw new IllegalArgumentException("Senha deve ter no mínimo 8 caracteres");
        }

        if (!senha.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("Senha deve conter pelo menos uma letra maiúscula");
        }

        if (!senha.matches(".*[a-z].*")) {
            throw new IllegalArgumentException("Senha deve conter pelo menos uma letra minúscula");
        }

        if (!senha.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Senha deve conter pelo menos um número");
        }
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
