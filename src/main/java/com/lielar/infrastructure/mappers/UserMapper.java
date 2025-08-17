package com.lielar.infrastructure.mappers;

import com.lielar.domain.dto.LoginResponseDTO;
import com.lielar.domain.entities.User;
import org.springframework.stereotype.Component;

/**
 * Mapeador para conversões entre User entity e DTOs
 */
@Component
public class UserMapper {

    /**
     * Converte User entity para LoginResponseDTO
     */
    public LoginResponseDTO toLoginResponseDTO(User user, String token) {
        if (user == null) {
            return null;
        }

        return LoginResponseDTO.builder()
                .token(token)
                .id(user.getId())
                .nome(user.getNome())
                .email(user.getEmail())
                .tipo(user.getTipo())
                .cargo(user.getCargo())
                .departamento(user.getDepartamento())
                .avatar(user.getAvatar())
                .ultimoLogin(user.getUltimoLogin())
                .permissoes(mapPermissoes(user.getPermissoes()))
                .build();
    }

    /**
     * Mapeia as permissões do usuário
     */
    private LoginResponseDTO.UserPermissoesDTO mapPermissoes(User.Permissoes permissoes) {
        if (permissoes == null) {
            return null;
        }

        return LoginResponseDTO.UserPermissoesDTO.builder()
                .produtos(permissoes.getProdutos())
                .categorias(permissoes.getCategorias())
                .usuarios(permissoes.getUsuarios())
                .orcamentos(permissoes.getOrcamentos())
                .relatorios(permissoes.getRelatorios())
                .build();
    }
}
