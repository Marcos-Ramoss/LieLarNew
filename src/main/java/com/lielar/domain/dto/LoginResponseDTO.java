package com.lielar.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para resposta de login
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {

    private String token;
    private String id;
    private String nome;
    private String email;
    private String tipo;
    private String cargo;
    private String departamento;
    private String avatar;
    private LocalDateTime ultimoLogin;
    private UserPermissoesDTO permissoes;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserPermissoesDTO {
        private Boolean produtos;
        private Boolean categorias;
        private Boolean usuarios;
        private Boolean orcamentos;
        private Boolean relatorios;
    }
}
