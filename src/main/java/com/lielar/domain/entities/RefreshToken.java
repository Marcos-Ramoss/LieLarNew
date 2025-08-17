package com.lielar.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

/**
 * Entidade para armazenar refresh tokens
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "refresh_tokens")
public class RefreshToken {

    @Id
    private String id;

    @Indexed(unique = true)
    private String token;

    private String userId;
    private String userEmail;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
    private Boolean revoked;

    // Método para verificar se o token expirou
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiresAt);
    }

    // Método para verificar se o token foi revogado
    public boolean isRevoked() {
        return Boolean.TRUE.equals(this.revoked);
    }

    // Método para verificar se o token é válido
    public boolean isValid() {
        return !isExpired() && !isRevoked();
    }
}
