package com.lielar.infrastructure.repositories;

import com.lielar.domain.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório para operações com usuários
 */
@Repository
public interface UserRepository extends MongoRepository<User, String>, BaseRepository<User, String> {

    /**
     * Busca usuário por email
     */
    Optional<User> findByEmail(String email);

    /**
     * Verifica se existe usuário com o email
     */
    boolean existsByEmail(String email);

    /**
     * Busca usuário ativo por email
     */
    Optional<User> findByEmailAndAtivoTrue(String email);
}
