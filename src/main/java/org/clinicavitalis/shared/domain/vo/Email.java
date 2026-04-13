package org.clinicavitalis.shared.domain.vo;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object para Email.
 * Responsável pela validação e imutabilidade de emails.
 */
public class Email {

    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private final String value;

    private Email(String value) {
        this.value = value;
    }

    /**
     * Factory method para criar um Email validado.
     * @param email endereço de email a validar
     * @return Email value object
     * @throws IllegalArgumentException se email inválido
     */
    public static Email of(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email não pode ser vazio");
        }

        String trimmed = email.trim().toLowerCase();

        if (!EMAIL_PATTERN.matcher(trimmed).matches()) {
            throw new IllegalArgumentException("Email inválido: " + email);
        }

        if (trimmed.length() > 150) {
            throw new IllegalArgumentException("Email não pode exceder 150 caracteres");
        }

        return new Email(trimmed);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(value, email.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
