package com.lielar.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuração para JWT
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    /**
     * Chave secreta para assinar JWT
     * Deve ter pelo menos 512 bits (64 bytes) para HS512
     */
    private String secret = "lieLarSecretKey2025SuperSecureAndLongEnoughForHS512AlgorithmWithMoreThan64BytesToEnsureSecurity";

    /**
     * Tempo de expiração do access token em segundos (padrão: 15 minutos)
     */
    private Long accessTokenExpiration = 900L; // 15 minutos

    /**
     * Tempo de expiração do refresh token em segundos (padrão: 7 dias)
     */
    private Long refreshTokenExpiration = 604800L; // 7 dias

    /**
     * Issuer do JWT
     */
    private String issuer = "Lie Lar Backend";

    /**
     * Audience do JWT
     */
    private String audience = "Lie Lar Users";
}
