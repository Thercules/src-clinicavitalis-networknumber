package org.clinicavitalis.usuario.domain.port;

import org.clinicavitalis.usuario.domain.entity.Usuario;

public interface TokenProvider {

    String generateToken(Usuario usuario);

    String generateRefreshToken(Usuario usuario);

    boolean isTokenValid(String token);

    boolean isRefreshToken(String token);

    Long getIdFromToken(String token);

    String getEmailFromToken(String token);

    String getNivelFromToken(String token);
}
