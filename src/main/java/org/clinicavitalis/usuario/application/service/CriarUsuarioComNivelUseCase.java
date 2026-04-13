package org.clinicavitalis.usuario.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.clinicavitalis.shared.domain.vo.CPF;
import org.clinicavitalis.shared.domain.vo.Email;
import org.clinicavitalis.shared.domain.vo.Telefone;
import org.clinicavitalis.usuario.application.dto.CreateUserRequest;
import org.clinicavitalis.usuario.application.dto.UsuarioResponse;
import org.clinicavitalis.usuario.domain.entity.NivelDeAcesso;
import org.clinicavitalis.usuario.domain.entity.Usuario;
import org.clinicavitalis.usuario.domain.port.PasswordHashingPort;
import org.clinicavitalis.usuario.domain.service.UsuarioService;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;

@ApplicationScoped
public class CriarUsuarioComNivelUseCase {

    @Inject
    UsuarioService usuarioDomainService;

    @Inject
    PasswordHashingPort passwordHasher;

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
    public UsuarioResponse executar(
        @Valid @NotNull CreateUserRequest request,
        Long usuarioAutenticadoId,
        String nivelDoUsuarioAutenticado
    ) {
        validarSenhas(request.getPassword(), request.getConfirmPassword());
        validarForcaSenha(request.getPassword());
        NivelDeAcesso.fromCodigo(request.getNivel_de_acesso());

        NivelDeAcesso nivelAutenticado = NivelDeAcesso.fromCodigo(nivelDoUsuarioAutenticado);
        NivelDeAcesso nivelASerCriado = NivelDeAcesso.fromCodigo(request.getNivel_de_acesso());

        if (!usuarioDomainService.validarHierarquiaDeAcesso(nivelAutenticado, nivelASerCriado))
            throw new org.clinicavitalis.usuario.domain.exception.UsuarioDomainException(
                "Sem permissão para criar usuário com nível de acesso: " + request.getNivel_de_acesso());

        Email email = Email.of(request.getEmail());
        Telefone telefone = parseTelefone(request.getTelefone());
        CPF cpf = parseCpf(request.getCpf());
        String senhaHasheada = passwordHasher.hash(request.getPassword());
        NivelDeAcesso nivelDeAcesso = NivelDeAcesso.fromCodigo(request.getNivel_de_acesso());

        Usuario usuario = Usuario.criarComNivel(
            email, senhaHasheada, request.getNome_completo(), telefone, cpf, nivelDeAcesso, usuarioAutenticadoId);

        usuario = usuarioDomainService.registrarUsuarioComNivel(usuario);

        return new UsuarioResponse(
            usuario.getId(),
            usuario.getEmail().getValue(),
            usuario.getNomeCompleto(),
            usuario.getNivelDeAcesso().getCodigo(),
            usuario.getEmailVerificado()
        );
    }

    private Telefone parseTelefone(String telefone) {
        if (telefone == null || telefone.isEmpty()) return null;
        try { return Telefone.of(telefone); } catch (IllegalArgumentException e) { return null; }
    }

    private CPF parseCpf(String cpf) {
        if (cpf == null || cpf.isEmpty()) return null;
        try { return CPF.of(cpf); } catch (IllegalArgumentException e) { return null; }
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
}
