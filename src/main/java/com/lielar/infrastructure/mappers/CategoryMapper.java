package com.lielar.infrastructure.mappers;

import com.lielar.domain.entities.Category;
import com.lielar.domain.dto.CategoryRequestDTO;
import com.lielar.domain.dto.CategoryResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapeador para conversão entre Category e DTOs
 */
@Component
public class CategoryMapper {

    /**
     * Converte CategoryRequestDTO para Category
     */
    public Category toEntity(CategoryRequestDTO dto) {
        Category category = Category.builder()
                .nome(dto.getNome())
                .descricao(dto.getDescricao())
                .icone(dto.getIcone() != null ? dto.getIcone() : "🏷️")
                .cor(dto.getCor() != null ? dto.getCor() : "#00d4ff")
                .ordem(dto.getOrdem() != null ? dto.getOrdem() : 0)
                .ativo(dto.getAtivo() != null ? dto.getAtivo() : true)
                .build();

        category.generateSlug();
        category.setCreatedAt();
        
        return category;
    }

    /**
     * Converte Category para CategoryResponseDTO
     */
    public CategoryResponseDTO toResponseDTO(Category category) {
        return CategoryResponseDTO.builder()
                .id(category.getId())
                .nome(category.getNome())
                .descricao(category.getDescricao())
                .icone(category.getIcone())
                .cor(category.getCor())
                .ordem(category.getOrdem())
                .ativo(category.getAtivo())
                .slug(category.getSlug())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }

    /**
     * Converte lista de Category para lista de CategoryResponseDTO
     */
    public List<CategoryResponseDTO> toResponseDTOList(List<Category> categories) {
        return categories.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Atualiza Category com dados do CategoryRequestDTO
     */
    public void updateEntityFromDTO(Category category, CategoryRequestDTO dto) {
        if (dto.getNome() != null) {
            category.setNome(dto.getNome());
            category.generateSlug();
        }
        if (dto.getDescricao() != null) {
            category.setDescricao(dto.getDescricao());
        }
        if (dto.getIcone() != null) {
            category.setIcone(dto.getIcone());
        }
        if (dto.getCor() != null) {
            category.setCor(dto.getCor());
        }
        if (dto.getOrdem() != null) {
            category.setOrdem(dto.getOrdem());
        }
        if (dto.getAtivo() != null) {
            category.setAtivo(dto.getAtivo());
        }
        
        category.setUpdatedAt();
    }
}
