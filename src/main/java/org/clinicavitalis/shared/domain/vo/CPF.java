package org.clinicavitalis.shared.domain.vo;

import java.util.Objects;

public class CPF {

    private final String value;

    private CPF(String value) {
        this.value = value;
    }

    public static CPF of(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("CPF não pode ser vazio");
        }

        String cleaned = cpf.replaceAll("\\D", "");

        if (cleaned.length() != 11) {
            throw new IllegalArgumentException("CPF deve conter 11 dígitos");
        }

        if (!isValidCPF(cleaned)) {
            throw new IllegalArgumentException("CPF inválido: " + cpf);
        }

        String formatted = String.format(
            "%s.%s.%s-%s",
            cleaned.substring(0, 3),
            cleaned.substring(3, 6),
            cleaned.substring(6, 9),
            cleaned.substring(9, 11)
        );

        return new CPF(formatted);
    }

    private static boolean isValidCPF(String cpf) {

        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            int sum = 0;
            int remainder;

            for (int i = 1; i <= 9; i++) {
                sum += Character.getNumericValue(cpf.charAt(i - 1)) * (11 - i);
            }

            remainder = (sum * 10) % 11;
            if (remainder == 10 || remainder == 11) {
                remainder = 0;
            }

            if (remainder != Character.getNumericValue(cpf.charAt(9))) {
                return false;
            }

            sum = 0;

            for (int i = 1; i <= 10; i++) {
                sum += Character.getNumericValue(cpf.charAt(i - 1)) * (12 - i);
            }

            remainder = (sum * 10) % 11;
            if (remainder == 10 || remainder == 11) {
                remainder = 0;
            }

            return remainder == Character.getNumericValue(cpf.charAt(10));
        } catch (Exception e) {
            return false;
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CPF cpf = (CPF) o;
        return Objects.equals(value, cpf.value);
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
