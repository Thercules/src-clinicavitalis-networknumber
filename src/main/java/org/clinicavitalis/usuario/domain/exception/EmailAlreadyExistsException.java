package org.clinicavitalis.usuario.domain.exception;

public class EmailAlreadyExistsException extends UsuarioDomainException {

    public EmailAlreadyExistsException(String email) {
        super("Email '" + email + "' já existente no sistema");
    }
}
