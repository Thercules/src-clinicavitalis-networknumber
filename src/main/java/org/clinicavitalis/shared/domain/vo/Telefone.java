package org.clinicavitalis.shared.domain.vo;

import java.util.Objects;

/**
 * Value Object para Telefone.
 * Responsável pela validação e imutabilidade de números de telefone.
 */
public class Telefone {

    private final String value;

    private Telefone(String value) {
        this.value = value;
    }

    /**
     * Factory method para criar um Telefone validado.
     * @param telefone número de telefone a validar
     * @return Telefone value object
     * @throws IllegalArgumentException se telefone inválido
     */
    public static Telefone of(String telefone) {
        if (telefone == null || telefone.trim().isEmpty()) {
            throw new IllegalArgumentException("Telefone não pode ser vazio");
        }

        // Remove caracteres não numéricos e parênteses
        String cleaned = telefone.replaceAll("[^0-9]", "");

        if (cleaned.length() < 10 || cleaned.length() > 11) {
            throw new IllegalArgumentException("Telefone deve conter 10 ou 11 dígitos");
        }

        if (telefone.length() > 20) {
            throw new IllegalArgumentException("Telefone não pode exceder 20 caracteres");
        }

        return new Telefone(telefone.trim());
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Telefone telefone = (Telefone) o;
        return Objects.equals(value, telefone.value);
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
