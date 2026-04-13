package org.clinicavitalis.usuario.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.clinicavitalis.usuario.domain.entity.Usuario;
import org.clinicavitalis.usuario.domain.port.TokenProvider;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class JwtTokenProvider implements TokenProvider {

    @ConfigProperty(name = "jwt.secret", defaultValue = "ClinicaVitalisSecureJwtSecretKeyFor512BitHS512AlgorithmMinimumRequirement2024JwtSecret")
    String jwtSecret;

    @ConfigProperty(name = "jwt.expiration.ms", defaultValue = "3600000")
    long jwtExpirationMs;

    @ConfigProperty(name = "jwt.refresh.expiration.ms", defaultValue = "604800000")
    long jwtRefreshExpirationMs;

    public String generateToken(Usuario usuario) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", usuario.getId());
        claims.put("email", usuario.getEmail().getValue());
        claims.put("nome", usuario.getNomeCompleto());
        claims.put("nivel", usuario.getNivelDeAcesso().getCodigo());

        return createToken(claims, usuario.getEmail().getValue(), jwtExpirationMs);
    }

    public String generateRefreshToken(Usuario usuario) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");
        claims.put("id", usuario.getId());

        return createToken(claims, usuario.getEmail().getValue(), jwtRefreshExpirationMs);
    }

    public Claims getClaims(String token) {
        return Jwts
            .parser()
            .verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    public String getEmailFromToken(String token) {
        return getClaims(token).getSubject();
    }

    public Long getIdFromToken(String token) {
        return getClaims(token).get("id", Long.class);
    }

    public String getNivelFromToken(String token) {
        return getClaims(token).get("nivel", String.class);
    }

    public boolean isRefreshToken(String token) {
        String type = getClaims(token).get("type", String.class);
        return "refresh".equals(type);
    }

    public boolean isTokenValid(String token) {
        try {
            Date expiration = getClaims(token).getExpiration();
            return expiration.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private String createToken(Map<String, Object> claims, String subject, long expirationTime) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), SignatureAlgorithm.HS512)
            .compact();
    }
}
