package com.lielar.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidade que representa um produto do sistema
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "products")
public class Product {

    @Id
    private String id;

    @Indexed(unique = true)
    private String codigo;

    private String nome;
    private String descricao;
    private BigDecimal preco;
    
    @DBRef
    private Category categoria;
    
    private String marca;
    private String modelo;
    
    @Builder.Default
    private Integer estoque = 0;
    
    @Builder.Default
    private String unidade = "un";
    
    private BigDecimal peso;
    private String dimensoes;
    
    @Builder.Default
    private Boolean ativo = true;
    
    @Builder.Default
    private Boolean destaque = false;
    
    private List<String> imagens;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Método para definir data de criação
    public void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Método para atualizar data de modificação
    public void setUpdatedAt() {
        this.updatedAt = LocalDateTime.now();
    }

    // Método para verificar se produto tem estoque
    public Boolean hasStock() {
        return this.estoque > 0;
    }

    // Método para verificar se produto está ativo
    public Boolean isActive() {
        return Boolean.TRUE.equals(this.ativo);
    }
}
