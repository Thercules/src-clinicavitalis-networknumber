package org.clinicavitalis.shared.domain.exception;

/**
 * Exceção lançada quando uma entidade solicitada não é encontrada.
 */
public class EntityNotFoundException extends DomainException {

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String entityName, Object id) {
        super(String.format("%s com ID %s não encontrado", entityName, id));
    }
}
