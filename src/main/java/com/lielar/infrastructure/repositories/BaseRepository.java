package com.lielar.infrastructure.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

/**
 * Repositório base com operações comuns para todas as entidades
 */
@NoRepositoryBean
public interface BaseRepository<T, ID> extends MongoRepository<T, ID> {

    /**
     * Busca entidades ativas
     */
    List<T> findByAtivoTrue();

    /**
     * Busca entidade ativa por ID
     */
    Optional<T> findByIdAndAtivoTrue(ID id);

    /**
     * Verifica se existe entidade ativa por ID
     */
    boolean existsByIdAndAtivoTrue(ID id);

    /**
     * Conta entidades ativas
     */
    long countByAtivoTrue();
}
