package com.lielar.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

/**
 * Entidade que representa uma categoria de produtos
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "categories")
public class Category {

    @Id
    private String id;

    @Indexed(unique = true)
    private String nome;

    private String descricao;
    
    @Builder.Default
    private String icone = "🏷️";
    
    @Builder.Default
    private String cor = "#00d4ff";
    
    @Builder.Default
    private Integer ordem = 0;
    
    @Builder.Default
    private Boolean ativo = true;

    @Indexed(unique = true)
    private String slug;

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

    // Método para gerar slug automaticamente
    public void generateSlug() {
        if (this.nome != null) {
            this.slug = this.nome.toLowerCase()
                    .replaceAll("[^a-z0-9\\s-]", "")
                    .replaceAll("\\s+", "-")
                    .trim();
        }
    }
}
