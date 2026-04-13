package org.clinicavitalis.usuario.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios", indexes = {
    @Index(name = "idx_email", columnList = "email"),
    @Index(name = "idx_nivel_acesso", columnList = "nivel_de_acesso"),
    @Index(name = "idx_ativo", columnList = "ativo"),
    @Index(name = "idx_cpf", columnList = "cpf")
})
public class UsuarioJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", length = 150, unique = true, nullable = false)
    private String email;

    @Column(name = "senha", length = 255, nullable = false)
    private String senha;

    @Column(name = "nome_completo", length = 200, nullable = false)
    private String nomeCompleto;

    @Column(name = "nivel_de_acesso", length = 20)
    @Enumerated(EnumType.STRING)
    private UsuarioNivelDeAcesso nivelDeAcesso;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao", nullable = false)
    private LocalDateTime dataAtualizacao;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    @Column(name = "ultimo_acesso")
    private LocalDateTime ultimoAcesso;

    @Column(name = "telefone", length = 20)
    private String telefone;

    @Column(name = "cpf", length = 14, unique = true)
    private String cpf;

    @Column(name = "reseta_senha_token", length = 255)
    private String resetaSenhaToken;

    @Column(name = "reseta_senha_expiracao")
    private LocalDateTime resetaSenhaExpiracao;

    @Column(name = "email_verificado", nullable = false)
    private Boolean emailVerificado = false;

    @Column(name = "verificacao_email_token", length = 255)
    private String verificacaoEmailToken;

    @Column(name = "criado_por")
    private Long criadoPor;

    public UsuarioJpaEntity() {
    }

    public UsuarioJpaEntity(
        String email,
        String senha,
        String nomeCompleto,
        UsuarioNivelDeAcesso nivelDeAcesso,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao,
        Boolean ativo,
        LocalDateTime ultimoAcesso,
        String telefone,
        String cpf,
        String resetaSenhaToken,
        LocalDateTime resetaSenhaExpiracao,
        Boolean emailVerificado,
        String verificacaoEmailToken,
        Long criadoPor
    ) {
        this.email = email;
        this.senha = senha;
        this.nomeCompleto = nomeCompleto;
        this.nivelDeAcesso = nivelDeAcesso;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
        this.ativo = ativo;
        this.ultimoAcesso = ultimoAcesso;
        this.telefone = telefone;
        this.cpf = cpf;
        this.resetaSenhaToken = resetaSenhaToken;
        this.resetaSenhaExpiracao = resetaSenhaExpiracao;
        this.emailVerificado = emailVerificado;
        this.verificacaoEmailToken = verificacaoEmailToken;
        this.criadoPor = criadoPor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public UsuarioNivelDeAcesso getNivelDeAcesso() {
        return nivelDeAcesso;
    }

    public void setNivelDeAcesso(UsuarioNivelDeAcesso nivelDeAcesso) {
        this.nivelDeAcesso = nivelDeAcesso;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDateTime getUltimoAcesso() {
        return ultimoAcesso;
    }

    public void setUltimoAcesso(LocalDateTime ultimoAcesso) {
        this.ultimoAcesso = ultimoAcesso;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getResetaSenhaToken() {
        return resetaSenhaToken;
    }

    public void setResetaSenhaToken(String resetaSenhaToken) {
        this.resetaSenhaToken = resetaSenhaToken;
    }

    public LocalDateTime getResetaSenhaExpiracao() {
        return resetaSenhaExpiracao;
    }

    public void setResetaSenhaExpiracao(LocalDateTime resetaSenhaExpiracao) {
        this.resetaSenhaExpiracao = resetaSenhaExpiracao;
    }

    public Boolean getEmailVerificado() {
        return emailVerificado;
    }

    public void setEmailVerificado(Boolean emailVerificado) {
        this.emailVerificado = emailVerificado;
    }

    public String getVerificacaoEmailToken() {
        return verificacaoEmailToken;
    }

    public void setVerificacaoEmailToken(String verificacaoEmailToken) {
        this.verificacaoEmailToken = verificacaoEmailToken;
    }

    public Long getCriadoPor() {
        return criadoPor;
    }

    public void setCriadoPor(Long criadoPor) {
        this.criadoPor = criadoPor;
    }

    public enum UsuarioNivelDeAcesso {
        GM, ADM, GESTOR, MEDICO, ENFERMEIRA, PACIENTE
    }
}
