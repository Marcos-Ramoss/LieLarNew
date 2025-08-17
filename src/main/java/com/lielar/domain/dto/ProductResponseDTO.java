package com.lielar.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para respostas de produtos
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {

    private String id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private String codigo;
    private CategoryResponseDTO categoria;
    private String marca;
    private String modelo;
    private Integer estoque;
    private String unidade;
    private BigDecimal peso;
    private String dimensoes;
    private Boolean ativo;
    private Boolean destaque;
    private List<String> imagens;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
