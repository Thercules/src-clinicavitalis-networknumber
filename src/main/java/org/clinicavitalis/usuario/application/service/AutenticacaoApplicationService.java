package org.clinicavitalis.usuario.application.service;

import org.clinicavitalis.shared.domain.vo.Email;
import org.clinicavitalis.shared.domain.vo.Telefone;
import org.clinicavitalis.usuario.application.dto.AuthTokenResponse;
import org.clinicavitalis.usuario.application.dto.LoginRequest;
import org.clinicavitalis.usuario.application.dto.RegisterRequest;
import org.clinicavitalis.usuario.application.dto.UsuarioResponse;
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

/**
 * Application Service: Autenticação
 * 
 * Orquestra os Use Cases de autenticação:
 * - Login
 * - Registro
 * - Refresh Token
 * 
 * Coordena entre:
 * - Domain Services (lógica de negócio)
 * - Repositories (persistência)
 * - Security utilities (criptografia, JWT)
 */
@ApplicationScoped
public class AutenticacaoApplicationService {

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    UsuarioDomainService usuarioDomainService;

    @Inject
    JwtTokenProvider jwtTokenProvider;

    /**
     * Use Case: Registrar novo usuário.
     * 
     * Fluxo:
     * 1. Valida dados da requisição
     * 2. Verifica se email já existe
     * 3. Cria entidade Usuario do domínio
     * 4. Persiste no banco
     * 5. Gera tokens JWT
     * 6. Retorna resposta com tokens
     * 
     * @param request dados de registro
     * @return resposta com tokens e dados do usuário
     */
    @Transactional
    public AuthTokenResponse registrar(@Valid @NotNull RegisterRequest request) {
        // Validações
        validarRegistro(request);

        // Value Objects
        Email email = Email.of(request.getEmail());

        Telefone telefone = null;
        if (request.getTelefone() != null && !request.getTelefone().isEmpty()) {
            try {
                telefone = Telefone.of(request.getTelefone());
            } catch (IllegalArgumentException e) {
                // telefone opcional, ignora erro de validação
            }
        }

        // Hash da senha
        String senhaHasheada = PasswordHasher.hash(request.getPassword());

        // Cria entidade de domínio
        Usuario usuario = Usuario.criar(
            email,
            senhaHasheada,
            request.getNome_completo(),
            telefone,
            null // CPF não é necessário para registro inicial
        );

        // Registra usando domain service (valida email único)
        usuario = usuarioDomainService.registrarNovoUsuario(usuario);

        // Gera tokens
        String token = jwtTokenProvider.generateToken(usuario);
        String refreshToken = jwtTokenProvider.generateRefreshToken(usuario);

        // Monta resposta
        UsuarioResponse usuarioResponse = mapToUsuarioResponse(usuario);
        AuthTokenResponse response = new AuthTokenResponse(token, refreshToken, usuarioResponse);

        return response;
    }

    /**
     * Use Case: Fazer login.
     * 
     * Fluxo:
     * 1. Valida dados da requisição
     * 2. Busca usuário por email
     * 3. Verifica se usuário está ativo
     * 4. Verifica senha
     * 5. Atualiza último acesso
     * 6. Gera tokens JWT
     * 7. Retorna resposta com tokens
     * 
     * @param request dados de login
     * @return resposta com tokens e dados do usuário
     */
    @Transactional
    public AuthTokenResponse login(@Valid @NotNull LoginRequest request) {
        // Busca usuário
        Email email = Email.of(request.getEmail());
        Usuario usuario = usuarioDomainService.encontrarPorEmail(email);

        // Valida se usuário está ativo
        if (!usuario.getAtivo()) {
            throw new org.clinicavitalis.usuario.domain.exception.UsuarioDomainException(
                "Usuário inativo"
            );
        }

        // Verifica senha
        usuario.verificarSenha(
            request.getPassword(),
            PasswordHasher::verify
        );

        // Atualiza último acesso
        usuario.marcarUltimoAcesso();
        usuario = usuarioRepository.atualizar(usuario);

        // Gera tokens
        String token = jwtTokenProvider.generateToken(usuario);
        String refreshToken = jwtTokenProvider.generateRefreshToken(usuario);

        // Monta resposta
        UsuarioResponse usuarioResponse = mapToUsuarioResponse(usuario);
        AuthTokenResponse response = new AuthTokenResponse(token, refreshToken, usuarioResponse);

        return response;
    }

    /**
     * Use Case: Renovar token (Refresh).
     * 
     * @param refreshToken refresh token fornecido
     * @return novo access token
     */
    public String renovarToken(String refreshToken) {
        // Valida refresh token
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

        // Busca usuário
        Long usuarioId = jwtTokenProvider.getIdFromToken(refreshToken);
        Usuario usuario = usuarioDomainService.encontrarPorId(usuarioId);

        // Gera novo token
        return jwtTokenProvider.generateToken(usuario);
    }

    // ========== Métodos Privados ==========

    /**
     * Valida dados de registro.
     */
    private void validarRegistro(RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Senhas não coincidem");
        }

        // Validações de senha forte
        validarForcaSenha(request.getPassword());
    }

    /**
     * Valida força da senha.
     * Padrão: mínimo 8 caracteres, letra maiúscula, minúscula, número.
     */
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

    /**
     * Mapeia Usuario para UsuarioResponse.
     */
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
