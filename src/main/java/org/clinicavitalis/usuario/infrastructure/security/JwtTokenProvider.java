package org.clinicavitalis.usuario.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.clinicavitalis.usuario.domain.entity.Usuario;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility para geração e validação de JWT (JSON Web Tokens).
 * 
 * Responsável por:
 * - Gerar tokens JWT para autenticação
 * - Validar e extrair claims de tokens
 */
@ApplicationScoped
public class JwtTokenProvider {

    @ConfigProperty(name = "jwt.secret", defaultValue = "ClinicaVitalisSecureJwtSecretKeyFor512BitHS512AlgorithmMinimumRequirement2024JwtSecret")
    String jwtSecret;

    @ConfigProperty(name = "jwt.expiration.ms", defaultValue = "3600000") // 1 hora
    long jwtExpirationMs;

    @ConfigProperty(name = "jwt.refresh.expiration.ms", defaultValue = "604800000") // 7 dias
    long jwtRefreshExpirationMs;

    /**
     * Gera um JWT token para autenticação.
     * 
     * @param usuario usuario para gerar token
     * @return JWT token assinado
     */
    public String generateToken(Usuario usuario) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", usuario.getId());
        claims.put("email", usuario.getEmail().getValue());
        claims.put("nome", usuario.getNomeCompleto());
        claims.put("nivel", usuario.getNivelDeAcesso().getCodigo());

        return createToken(claims, usuario.getEmail().getValue(), jwtExpirationMs);
    }

    /**
     * Gera um JWT refresh token com validade maior.
     * 
     * @param usuario usuario para gerar refresh token
     * @return JWT refresh token assinado
     */
    public String generateRefreshToken(Usuario usuario) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");
        claims.put("id", usuario.getId());

        return createToken(claims, usuario.getEmail().getValue(), jwtRefreshExpirationMs);
    }

    /**
     * Valida um token JWT e extrai os claims.
     * 
     * @param token JWT token a validar
     * @return Claims do token
     * @throws io.jsonwebtoken.JwtException se token inválido ou expirado
     */
    public Claims getClaims(String token) {
        return Jwts
            .parser()
            .verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    /**
     * Extrai o email (subject) do token.
     * 
     * @param token JWT token
     * @return email do subject
     */
    public String getEmailFromToken(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Extrai o ID do usuário do token.
     * 
     * @param token JWT token
     * @return ID do usuário
     */
    public Long getIdFromToken(String token) {
        return getClaims(token).get("id", Long.class);
    }

    /**
     * Extrai o nível de acesso do token.
     * 
     * @param token JWT token
     * @return nível de acesso (código)
     */
    public String getNivelFromToken(String token) {
        return getClaims(token).get("nivel", String.class);
    }

    /**
     * Verifica se um token é de refresh.
     * 
     * @param token JWT token
     * @return true se é refresh token
     */
    public boolean isRefreshToken(String token) {
        String type = getClaims(token).get("type", String.class);
        return "refresh".equals(type);
    }

    /**
     * Valida se um token não está expirado.
     * 
     * @param token JWT token
     * @return true se token ainda é válido
     */
    public boolean isTokenValid(String token) {
        try {
            Date expiration = getClaims(token).getExpiration();
            return expiration.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // ========== Métodos Privados ==========

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
