package org.clinicavitalis.shared.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * DTO wrapper para resposta padrão de API.
 * 
 * Segue o padrão de resposta especificado no DATABASE_AUTH_DESIGN.md
 * 
 * @param <T> tipo dos dados da resposta
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private Boolean sucesso;
    private String mensagem;
    private T dados;

    // ========== Construtores ==========

    public ApiResponse() {
    }

    public ApiResponse(Boolean sucesso, String mensagem) {
        this.sucesso = sucesso;
        this.mensagem = mensagem;
    }

    public ApiResponse(Boolean sucesso, String mensagem, T dados) {
        this.sucesso = sucesso;
        this.mensagem = mensagem;
        this.dados = dados;
    }

    // ========== Factory Methods ==========

    /**
     * Cria uma resposta de sucesso com dados.
     */
    public static <T> ApiResponse<T> success(String mensagem, T dados) {
        return new ApiResponse<>(true, mensagem, dados);
    }

    /**
     * Cria uma resposta de sucesso sem dados.
     */
    public static <T> ApiResponse<T> success(String mensagem) {
        return new ApiResponse<>(true, mensagem);
    }

    /**
     * Cria uma resposta de erro.
     */
    public static <T> ApiResponse<T> error(String mensagem) {
        return new ApiResponse<>(false, mensagem);
    }

    // ========== Getters & Setters ==========

    public Boolean getSucesso() {
        return sucesso;
    }

    public void setSucesso(Boolean sucesso) {
        this.sucesso = sucesso;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public T getDados() {
        return dados;
    }

    public void setDados(T dados) {
        this.dados = dados;
    }
}
