package org.clinicavitalis.usuario.domain.exception;

public class CredenciaisInvalidasException extends UsuarioDomainException {

    public CredenciaisInvalidasException() {
        super("Email ou senha incorretos");
    }
}
