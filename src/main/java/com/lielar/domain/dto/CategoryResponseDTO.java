package com.lielar.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para respostas de categorias
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDTO {

    private String id;
    private String nome;
    private String descricao;
    private String icone;
    private String cor;
    private Integer ordem;
    private Boolean ativo;
    private String slug;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
