package org.clinicavitalis.shared.domain.exception;

/**
 * Exceção base para todas as exceções de domínio.
 * Representa erros relacionados às regras de negócio.
 */
public abstract class DomainException extends RuntimeException {

    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
