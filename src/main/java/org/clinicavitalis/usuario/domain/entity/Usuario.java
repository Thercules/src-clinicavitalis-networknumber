package org.clinicavitalis.usuario.domain.entity;

import org.clinicavitalis.shared.domain.vo.CPF;
import org.clinicavitalis.shared.domain.vo.Email;
import org.clinicavitalis.shared.domain.vo.Telefone;
import org.clinicavitalis.usuario.domain.exception.CredenciaisInvalidasException;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidade de Domínio: Usuario
 * 
 * Agregado raiz que representa um usuário do sistema.
 * Encapsula toda a lógica de negócio relacionada a usuários.
 * 
 * Segue os princípios de Domain-Driven Design com:
 * - Identidade única (id)
 * - Validação de invariantes
 * - Value Objects para campos complexos
 */
public class Usuario {

    private Long id;
    private Email email;
    private String senha; // sempre hasheada
    private String nomeCompleto;
    private NivelDeAcesso nivelDeAcesso;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private Boolean ativo;
    private LocalDateTime ultimoAcesso;
    private Telefone telefone;
    private CPF cpf;
    private String resetaSenhaToken;
    private LocalDateTime resetaSenhaExpiracao;
    private Boolean emailVerificado;
    private String verificacaoEmailToken;
    private Long criadoPor;

    // Construtor privado para criar apenas via factory methods
    private Usuario() {
    }

    /**
     * Factory method para criar um novo usuário (registro/cadastro).
     * Aplica validações de negócio para um novo usuário.
     * 
     * @param email email do usuário
     * @param senhaHasheada senha já hasheada (bcrypt)
     * @param nomeCompleto nome completo
     * @param telefone telefone (opcional)
     * @param cpf CPF (opcional)
     * @return nova instância de Usuario
     */
    public static Usuario criar(
        Email email,
        String senhaHasheada,
        String nomeCompleto,
        Telefone telefone,
        CPF cpf
    ) {
        Objects.requireNonNull(email, "Email é obrigatório");
        Objects.requireNonNull(senhaHasheada, "Senha é obrigatória");
        Objects.requireNonNull(nomeCompleto, "Nome completo é obrigatório");

        if (nomeCompleto.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome completo não pode ser vazio");
        }

        if (nomeCompleto.length() > 200) {
            throw new IllegalArgumentException("Nome completo não pode exceder 200 caracteres");
        }

        Usuario usuario = new Usuario();
        usuario.email = email;
        usuario.senha = senhaHasheada;
        usuario.nomeCompleto = nomeCompleto;
        usuario.nivelDeAcesso = NivelDeAcesso.PACIENTE; // default
        usuario.dataCriacao = LocalDateTime.now();
        usuario.dataAtualizacao = LocalDateTime.now();
        usuario.ativo = true;
        usuario.emailVerificado = false;
        usuario.telefone = telefone;
        usuario.cpf = cpf;

        return usuario;
    }

    /**
     * Factory method para criar um novo usuário com nível de acesso específico.
     * Utilizado por usuários GM para criar usuários com diferentes níveis.
     * 
     * @param email email do usuário
     * @param senhaHasheada senha já hasheada (bcrypt)
     * @param nomeCompleto nome completo
     * @param telefone telefone (opcional)
     * @param cpf CPF (opcional)
     * @param nivelDeAcesso nível de acesso desejado
     * @param criadoPor ID do usuário que criou este registro (para auditoria)
     * @return nova instância de Usuario
     */
    public static Usuario criarComNivel(
        Email email,
        String senhaHasheada,
        String nomeCompleto,
        Telefone telefone,
        CPF cpf,
        NivelDeAcesso nivelDeAcesso,
        Long criadoPor
    ) {
        Objects.requireNonNull(email, "Email é obrigatório");
        Objects.requireNonNull(senhaHasheada, "Senha é obrigatória");
        Objects.requireNonNull(nomeCompleto, "Nome completo é obrigatório");
        Objects.requireNonNull(nivelDeAcesso, "Nível de acesso é obrigatório");

        if (nomeCompleto.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome completo não pode ser vazio");
        }

        if (nomeCompleto.length() > 200) {
            throw new IllegalArgumentException("Nome completo não pode exceder 200 caracteres");
        }

        Usuario usuario = new Usuario();
        usuario.email = email;
        usuario.senha = senhaHasheada;
        usuario.nomeCompleto = nomeCompleto;
        usuario.nivelDeAcesso = nivelDeAcesso;
        usuario.dataCriacao = LocalDateTime.now();
        usuario.dataAtualizacao = LocalDateTime.now();
        usuario.ativo = true;
        usuario.emailVerificado = false;
        usuario.telefone = telefone;
        usuario.cpf = cpf;
        usuario.criadoPor = criadoPor;

        return usuario;
    }

    /**
     * Factory method para reconectar um usuário carregado do banco de dados.
     */
    public static Usuario reconectar(
        Long id,
        Email email,
        String senhaHasheada,
        String nomeCompleto,
        NivelDeAcesso nivelDeAcesso,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao,
        Boolean ativo,
        LocalDateTime ultimoAcesso,
        Telefone telefone,
        CPF cpf,
        String resetaSenhaToken,
        LocalDateTime resetaSenhaExpiracao,
        Boolean emailVerificado,
        String verificacaoEmailToken,
        Long criadoPor
    ) {
        Usuario usuario = new Usuario();
        usuario.id = id;
        usuario.email = email;
        usuario.senha = senhaHasheada;
        usuario.nomeCompleto = nomeCompleto;
        usuario.nivelDeAcesso = nivelDeAcesso;
        usuario.dataCriacao = dataCriacao;
        usuario.dataAtualizacao = dataAtualizacao;
        usuario.ativo = ativo;
        usuario.ultimoAcesso = ultimoAcesso;
        usuario.telefone = telefone;
        usuario.cpf = cpf;
        usuario.resetaSenhaToken = resetaSenhaToken;
        usuario.resetaSenhaExpiracao = resetaSenhaExpiracao;
        usuario.emailVerificado = emailVerificado;
        usuario.verificacaoEmailToken = verificacaoEmailToken;
        usuario.criadoPor = criadoPor;
        return usuario;
    }

    /**
     * Verifica se a senha fornecida corresponde à senha armazenada.
     * Esta lógica seria delegada ao security service na prática.
     * 
     * @param senhaFornecida senha em texto plano fornecida pelo usuário
     * @param verificadorSenha função que verifica a senha (bcrypt)
     * @return true se a senha é válida
     * @throws CredenciaisInvalidasException se a senha for inválida
     */
    public void verificarSenha(String senhaFornecida, VerificadorSenha verificadorSenha) {
        if (!verificadorSenha.verificar(senhaFornecida, this.senha)) {
            throw new CredenciaisInvalidasException();
        }
    }

    /**
     * Marca o último acesso do usuário.
     */
    public void marcarUltimoAcesso() {
        this.ultimoAcesso = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
    }

    /**
     * Define se o usuário está ativo.
     */
    public void ativar() {
        this.ativo = true;
        this.dataAtualizacao = LocalDateTime.now();
    }

    /**
     * Desativa o usuário (soft-delete).
     */
    public void desativar() {
        this.ativo = false;
        this.dataAtualizacao = LocalDateTime.now();
    }

    /**
     * Verifica o email do usuário.
     */
    public void verificarEmail() {
        this.emailVerificado = true;
        this.verificacaoEmailToken = null;
        this.dataAtualizacao = LocalDateTime.now();
    }

    /**
     * Define token e data de expiração para reset de senha.
     */
    public void gerarTokenResetSenha(String token, LocalDateTime expiracaoToken) {
        this.resetaSenhaToken = token;
        this.resetaSenhaExpiracao = expiracaoToken;
        this.dataAtualizacao = LocalDateTime.now();
    }

    /**
     * Reseta a senha do usuário.
     */
    public void resetarSenha(String novaSenhaHasheada) {
        this.senha = novaSenhaHasheada;
        this.resetaSenhaToken = null;
        this.resetaSenhaExpiracao = null;
        this.dataAtualizacao = LocalDateTime.now();
    }

    // ========== Getters ==========

    public Long getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public NivelDeAcesso getNivelDeAcesso() {
        return nivelDeAcesso;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public LocalDateTime getUltimoAcesso() {
        return ultimoAcesso;
    }

    public Telefone getTelefone() {
        return telefone;
    }

    public CPF getCpf() {
        return cpf;
    }

    public String getResetaSenhaToken() {
        return resetaSenhaToken;
    }

    public LocalDateTime getResetaSenhaExpiracao() {
        return resetaSenhaExpiracao;
    }

    public Boolean getEmailVerificado() {
        return emailVerificado;
    }

    public String getVerificacaoEmailToken() {
        return verificacaoEmailToken;
    }

    public Long getCriadoPor() {
        return criadoPor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id) && Objects.equals(email, usuario.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    @Override
    public String toString() {
        return "Usuario{" +
            "id=" + id +
            ", email=" + email +
            ", nomeCompleto='" + nomeCompleto + '\'' +
            ", nivelDeAcesso=" + nivelDeAcesso +
            ", ativo=" + ativo +
            '}';
    }

    /**
     * Interface para estratégia de verificação de senha.
     * Promove inversão de dependência.
     */
    @FunctionalInterface
    public interface VerificadorSenha {
        boolean verificar(String senhaFornecida, String senhaHasheada);
    }
}
