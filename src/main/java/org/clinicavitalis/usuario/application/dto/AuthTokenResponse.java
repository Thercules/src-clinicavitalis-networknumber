package org.clinicavitalis.usuario.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthTokenResponse {

    private String token;

    @JsonProperty("refresh_token")
    private String refresh_token;

    private UsuarioResponse usuario;

    public AuthTokenResponse() {
    }

    public AuthTokenResponse(String token, String refresh_token, UsuarioResponse usuario) {
        this.token = token;
        this.refresh_token = refresh_token;
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public UsuarioResponse getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioResponse usuario) {
        this.usuario = usuario;
    }
}
