package com.lielar.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para resposta JWT com access token e refresh token
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponseDTO {

    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private Long expiresIn; // em segundos
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
