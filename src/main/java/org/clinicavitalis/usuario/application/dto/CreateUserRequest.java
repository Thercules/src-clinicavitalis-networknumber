package org.clinicavitalis.usuario.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para requisição de criação de usuário com nível de acesso.
 * 
 * Uso: Apenas por usuários GM para criar usuários com diferentes níveis.
 * Permite definir o nível de acesso explicitamente.
 */
public class CreateUserRequest {

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
    private String password;

    @NotBlank(message = "Confirmação de senha é obrigatória")
    private String confirmPassword;

    @NotBlank(message = "Nome completo é obrigatório")
    @Size(min = 3, max = 200, message = "Nome deve ter entre 3 e 200 caracteres")
    private String nome_completo;

    @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
    private String telefone;

    @Size(max = 18, message = "CPF deve ter no máximo 18 caracteres")
    @JsonProperty("cpf")
    private String cpf;

    @NotBlank(message = "Nível de acesso é obrigatório")
    @JsonProperty("nivel_de_acesso")
    private String nivel_de_acesso;

    // ========== Construtores ==========

    public CreateUserRequest() {
    }

    public CreateUserRequest(String email, String password, String confirmPassword, 
                           String nome_completo, String telefone, String cpf, 
                           String nivel_de_acesso) {
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.nome_completo = nome_completo;
        this.telefone = telefone;
        this.cpf = cpf;
        this.nivel_de_acesso = nivel_de_acesso;
    }

    // ========== Getters & Setters ==========

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getNome_completo() {
        return nome_completo;
    }

    public void setNome_completo(String nome_completo) {
        this.nome_completo = nome_completo;
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

    public String getNivel_de_acesso() {
        return nivel_de_acesso;
    }

    public void setNivel_de_acesso(String nivel_de_acesso) {
        this.nivel_de_acesso = nivel_de_acesso;
    }
}
