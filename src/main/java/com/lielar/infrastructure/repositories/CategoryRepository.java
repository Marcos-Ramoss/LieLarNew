package com.lielar.infrastructure.repositories;

import com.lielar.domain.entities.Category;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para operações com categorias
 */
@Repository
public interface CategoryRepository extends BaseRepository<Category, String> {

    /**
     * Busca categoria por nome
     */
    Optional<Category> findByNome(String nome);

    /**
     * Busca categoria por slug
     */
    Optional<Category> findBySlug(String slug);

    /**
     * Verifica se existe categoria com nome
     */
    boolean existsByNome(String nome);

    /**
     * Verifica se existe categoria com slug
     */
    boolean existsBySlug(String slug);

    /**
     * Busca categorias ordenadas por ordem
     */
    List<Category> findByAtivoTrueOrderByOrdemAsc();

    /**
     * Busca categorias por nome contendo (case insensitive)
     */
    List<Category> findByNomeContainingIgnoreCaseAndAtivoTrue(String nome);
}
