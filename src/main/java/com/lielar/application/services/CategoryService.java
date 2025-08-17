package com.lielar.application.services;

import com.lielar.domain.entities.Category;
import com.lielar.domain.dto.CategoryRequestDTO;
import com.lielar.domain.dto.CategoryResponseDTO;
import com.lielar.infrastructure.repositories.CategoryRepository;
import com.lielar.infrastructure.mappers.CategoryMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Serviço de negócio para operações com categorias
 */
@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    /**
     * Cria uma nova categoria
     */
    public CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO) {
        // Valida se já existe categoria com o mesmo nome
        if (categoryRepository.existsByNome(requestDTO.getNome())) {
            throw new RuntimeException("Já existe uma categoria com o nome: " + requestDTO.getNome());
        }

        Category category = categoryMapper.toEntity(requestDTO);
        Category savedCategory = categoryRepository.save(category);
        
        return categoryMapper.toResponseDTO(savedCategory);
    }

    /**
     * Busca categoria por ID
     */
    @Transactional(readOnly = true)
    public Optional<CategoryResponseDTO> findCategoryById(String id) {
        return categoryRepository.findByIdAndAtivoTrue(id)
                .map(categoryMapper::toResponseDTO);
    }

    /**
     * Busca categoria por slug
     */
    @Transactional(readOnly = true)
    public Optional<CategoryResponseDTO> findCategoryBySlug(String slug) {
        return categoryRepository.findBySlug(slug)
                .filter(Category::getAtivo)
                .map(categoryMapper::toResponseDTO);
    }

    /**
     * Lista todas as categorias ativas
     */
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> findAllActiveCategories() {
        List<Category> categories = categoryRepository.findByAtivoTrueOrderByOrdemAsc();
        return categoryMapper.toResponseDTOList(categories);
    }

    /**
     * Busca categorias por nome
     */
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> findCategoriesByName(String nome) {
        List<Category> categories = categoryRepository.findByNomeContainingIgnoreCaseAndAtivoTrue(nome);
        return categoryMapper.toResponseDTOList(categories);
    }

    /**
     * Atualiza uma categoria existente
     */
    public Optional<CategoryResponseDTO> updateCategory(String id, CategoryRequestDTO requestDTO) {
        return categoryRepository.findByIdAndAtivoTrue(id)
                .map(category -> {
                    // Verifica se o novo nome já existe em outra categoria
                    if (!category.getNome().equals(requestDTO.getNome()) && 
                        categoryRepository.existsByNome(requestDTO.getNome())) {
                        throw new RuntimeException("Já existe uma categoria com o nome: " + requestDTO.getNome());
                    }
                    
                    categoryMapper.updateEntityFromDTO(category, requestDTO);
                    Category updatedCategory = categoryRepository.save(category);
                    return categoryMapper.toResponseDTO(updatedCategory);
                });
    }

    /**
     * Desativa uma categoria (soft delete)
     */
    public Optional<CategoryResponseDTO> deactivateCategory(String id) {
        return categoryRepository.findByIdAndAtivoTrue(id)
                .map(category -> {
                    category.setAtivo(false);
                    category.setUpdatedAt();
                    Category deactivatedCategory = categoryRepository.save(category);
                    return categoryMapper.toResponseDTO(deactivatedCategory);
                });
    }

    /**
     * Ativa uma categoria
     */
    public Optional<CategoryResponseDTO> activateCategory(String id) {
        return categoryRepository.findById(id)
                .map(category -> {
                    category.setAtivo(true);
                    category.setUpdatedAt();
                    Category activatedCategory = categoryRepository.save(category);
                    return categoryMapper.toResponseDTO(activatedCategory);
                });
    }

    /**
     * Verifica se categoria existe e está ativa
     */
    @Transactional(readOnly = true)
    public boolean existsAndActive(String id) {
        return categoryRepository.existsByIdAndAtivoTrue(id);
    }

    /**
     * Conta categorias ativas
     */
    @Transactional(readOnly = true)
    public long countActiveCategories() {
        return categoryRepository.countByAtivoTrue();
    }
}
