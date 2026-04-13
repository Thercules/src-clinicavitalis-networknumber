package org.clinicavitalis.usuario.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UsuarioResponse {

    private Long id;

    private String email;

    @JsonProperty("nome_completo")
    private String nomeCompleto;

    @JsonProperty("nivel_de_acesso")
    private String nivelDeAcesso;

    @JsonProperty("email_verificado")
    private Boolean emailVerificado;

    public UsuarioResponse() {
    }

    public UsuarioResponse(Long id, String email, String nomeCompleto, String nivelDeAcesso, Boolean emailVerificado) {
        this.id = id;
        this.email = email;
        this.nomeCompleto = nomeCompleto;
        this.nivelDeAcesso = nivelDeAcesso;
        this.emailVerificado = emailVerificado;
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

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getNivelDeAcesso() {
        return nivelDeAcesso;
    }

    public void setNivelDeAcesso(String nivelDeAcesso) {
        this.nivelDeAcesso = nivelDeAcesso;
    }

    public Boolean getEmailVerificado() {
        return emailVerificado;
    }

    public void setEmailVerificado(Boolean emailVerificado) {
        this.emailVerificado = emailVerificado;
    }
}
