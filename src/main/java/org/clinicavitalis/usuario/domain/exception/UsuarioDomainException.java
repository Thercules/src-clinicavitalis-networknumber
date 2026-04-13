package org.clinicavitalis.usuario.domain.exception;

import org.clinicavitalis.shared.domain.exception.DomainException;

/**
 * Exceção lançada quando há violação de regras do domínio de usuários.
 */
public class UsuarioDomainException extends DomainException {

    public UsuarioDomainException(String message) {
        super(message);
    }

    public UsuarioDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
