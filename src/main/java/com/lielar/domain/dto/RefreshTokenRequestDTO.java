package com.lielar.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para requisição de refresh token
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenRequestDTO {

    @NotBlank(message = "Refresh token é obrigatório")
    private String refreshToken;
}
