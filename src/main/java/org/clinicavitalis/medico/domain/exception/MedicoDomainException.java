package org.clinicavitalis.medico.domain.exception;

import org.clinicavitalis.shared.domain.exception.DomainException;

public class MedicoDomainException extends DomainException {
    public MedicoDomainException(String message) { super(message); }
    public MedicoDomainException(String message, Throwable cause) { super(message, cause); }
}
