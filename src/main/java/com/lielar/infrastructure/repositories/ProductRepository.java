package com.lielar.infrastructure.repositories;

import com.lielar.domain.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para operações com produtos
 */
@Repository
public interface ProductRepository extends BaseRepository<Product, String> {

    /**
     * Busca produto por código
     */
    Optional<Product> findByCodigo(String codigo);

    /**
     * Verifica se existe produto com código
     */
    boolean existsByCodigo(String codigo);

    /**
     * Busca produtos por categoria
     */
    List<Product> findByCategoriaIdAndAtivoTrue(String categoriaId);

    /**
     * Busca produtos em destaque
     */
    List<Product> findByDestaqueTrueAndAtivoTrue();

    /**
     * Busca produtos por nome contendo (case insensitive)
     */
    List<Product> findByNomeContainingIgnoreCaseAndAtivoTrue(String nome);

    /**
     * Busca produtos por código contendo (case insensitive)
     */
    List<Product> findByCodigoContainingIgnoreCaseAndAtivoTrue(String codigo);

    /**
     * Busca produtos com estoque baixo (menor que quantidade especificada)
     */
    List<Product> findByEstoqueLessThanAndAtivoTrue(Integer quantidade);

    /**
     * Busca produtos com paginação
     */
    Page<Product> findByAtivoTrue(Pageable pageable);

    /**
     * Busca produtos por categoria com paginação
     */
    Page<Product> findByCategoriaIdAndAtivoTrue(String categoriaId, Pageable pageable);
}
