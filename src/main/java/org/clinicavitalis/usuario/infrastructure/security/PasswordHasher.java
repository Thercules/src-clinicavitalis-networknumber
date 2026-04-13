package org.clinicavitalis.usuario.infrastructure.security;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * Utility para hash e verificação de senhas usando BCrypt.
 * 
 * Responsável por:
 * - Fazer hash de senhas
 * - Verificar senhas contra hash
 * 
 * Implementa a interface VerificadorSenha da entidade Usuario.
 */
public class PasswordHasher {

    private static final int COST = 10; // Custo do BCrypt (10-12 recomendado)

    private PasswordHasher() {
        // Private constructor to prevent instantiation
    }

    /**
     * Realiza hash de uma senha em texto plano.
     * 
     * @param password senha em texto plano
     * @return senha hasheada
     */
    public static String hash(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Senha não pode ser vazia");
        }
        return BCrypt.withDefaults().hashToString(COST, password.toCharArray());
    }

    /**
     * Verifica se uma senha em texto plano corresponde ao hash.
     * 
     * @param password senha em texto plano fornecida pelo usuário
     * @param hashedPassword hash da senha armazenado
     * @return true se correspondem
     */
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
