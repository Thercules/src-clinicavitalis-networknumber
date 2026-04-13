package org.clinicavitalis.usuario.domain.port;

public interface PasswordHashingPort {

    String hash(String plainPassword);

    boolean verify(String plainPassword, String hashedPassword);
}
