package com.lielar.application.services;

import com.lielar.domain.entities.User;
import com.lielar.infrastructure.config.JwtConfig;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Serviço para operações com JWT
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtConfig jwtConfig;

    /**
     * Gera access token para um usuário
     */
    public String generateAccessToken(User user) {
        return generateToken(user, jwtConfig.getAccessTokenExpiration());
    }

    /**
     * Gera refresh token para um usuário
     */
    public String generateRefreshToken(User user) {
        return generateToken(user, jwtConfig.getRefreshTokenExpiration());
    }

    /**
     * Gera token JWT
     */
    private String generateToken(User user, Long expirationSeconds) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());
        claims.put("tipo", user.getTipo());
        claims.put("permissoes", user.getPermissoes());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuer(jwtConfig.getIssuer())
                .setAudience(jwtConfig.getAudience())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(
                        LocalDateTime.now()
                                .plusSeconds(expirationSeconds)
                                .atZone(ZoneId.systemDefault())
                                .toInstant()
                ))
                .signWith(getSigningKey(), Jwts.SIG.HS512)
                .compact();
    }

    /**
     * Extrai email do token
     */
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrai ID do usuário do token
     */
    public String extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", String.class));
    }

    /**
     * Extrai data de expiração do token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extrai claim específico do token
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extrai todos os claims do token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Verifica se o token expirou
     */
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = extractExpiration(token);
            return expiration.before(new Date());
        } catch (Exception e) {
            log.warn("Erro ao verificar expiração do token: {}", e.getMessage());
            return true;
        }
    }

    /**
     * Verifica se o token é válido para um usuário
     */
    public boolean isTokenValid(String token, User user) {
        try {
            final String email = extractEmail(token);
            return (email.equals(user.getEmail()) && !isTokenExpired(token));
        } catch (Exception e) {
            log.warn("Erro ao validar token para usuário {}: {}", user.getEmail(), e.getMessage());
            return false;
        }
    }

    /**
     * Valida se um token é válido
     */
    public boolean isValidToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Token inválido: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Obtém a chave de assinatura
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Calcula tempo restante do token em segundos
     */
    public long getTimeUntilExpiration(String token) {
        try {
            Date expiration = extractExpiration(token);
            Date now = new Date();
            return Math.max(0, (expiration.getTime() - now.getTime()) / 1000);
        } catch (Exception e) {
            log.warn("Erro ao calcular tempo de expiração: {}", e.getMessage());
            return 0;
        }
    }
}
