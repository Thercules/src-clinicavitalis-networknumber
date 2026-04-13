package org.clinicavitalis.usuario.infrastructure.security;

import jakarta.enterprise.context.ApplicationScoped;
import org.clinicavitalis.usuario.domain.port.PasswordHashingPort;

@ApplicationScoped
public class PasswordHashingAdapter implements PasswordHashingPort {

    @Override
    public String hash(String plainPassword) {
        return PasswordHasher.hash(plainPassword);
    }

    @Override
    public boolean verify(String plainPassword, String hashedPassword) {
        return PasswordHasher.verify(plainPassword, hashedPassword);
    }
}
