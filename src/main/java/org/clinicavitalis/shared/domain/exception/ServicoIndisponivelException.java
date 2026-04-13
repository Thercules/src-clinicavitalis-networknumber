package org.clinicavitalis.shared.domain.exception;

/**
 * Lançada pelo fallback do Circuit Breaker quando o serviço está
 * temporariamente indisponível (circuito aberto).
 */
public class ServicoIndisponivelException extends DomainException {
    public ServicoIndisponivelException(String message) { super(message); }
}
