package com.lielar.infrastructure.repositories;

import com.lielar.domain.entities.RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório para operações com refresh tokens
 */
@Repository
public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {

    /**
     * Busca refresh token por token
     */
    Optional<RefreshToken> findByToken(String token);

    /**
     * Busca refresh token por usuário
     */
    Optional<RefreshToken> findByUserId(String userId);

    /**
     * Verifica se existe refresh token para um usuário
     */
    boolean existsByUserId(String userId);

    /**
     * Remove todos os refresh tokens de um usuário
     */
    void deleteByUserId(String userId);

    /**
     * Remove refresh token por token
     */
    void deleteByToken(String token);
}
