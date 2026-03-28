package org.clinicavitalis.usuario.domain.exception;

/**
 * Exceção lançada quando credenciais de login são inválidas.
 */
public class CredenciaisInvalidasException extends UsuarioDomainException {

    public CredenciaisInvalidasException() {
        super("Email ou senha incorretos");
    }
}
