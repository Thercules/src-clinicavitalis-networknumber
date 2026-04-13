package org.clinicavitalis.usuario.infrastructure.security;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordHasher {

    private static final int COST = 10;

    private PasswordHasher() {

    }

    public static String hash(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Senha não pode ser vazia");
        }
        return BCrypt.withDefaults().hashToString(COST, password.toCharArray());
    }

    public static boolean verify(String password, String hashedPassword) {
        if (password == null || hashedPassword == null) {
            return false;
        }
        try {
            BCrypt.Result result = BCrypt.verifyer().verify(
                password.toCharArray(),
                hashedPassword
            );
            return result.verified;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
