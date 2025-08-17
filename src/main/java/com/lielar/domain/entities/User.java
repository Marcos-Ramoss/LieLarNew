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
 * Entidade que representa um usuário do sistema
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    private String email;

    private String nome;
    private String senha;
    private String tipo; // admin, gerente, funcionario, vendedor
    private String cargo;
    private String departamento; // administrativo, vendas, estoque, financeiro, ti
    private String telefone;
    private String avatar;
    
    @Builder.Default
    private Boolean ativo = true;
    
    private LocalDateTime ultimoLogin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Permissões do usuário
    private Permissoes permissoes;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Permissoes {
        private Boolean produtos;
        private Boolean categorias;
        private Boolean usuarios;
        private Boolean orcamentos;
        private Boolean relatorios;
    }

    // Método para definir data de criação
    public void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Método para atualizar data de modificação
    public void setUpdatedAt() {
        this.updatedAt = LocalDateTime.now();
    }
}
