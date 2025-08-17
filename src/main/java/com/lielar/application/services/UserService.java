package com.lielar.application.services;

import com.lielar.domain.dto.LoginRequestDTO;
import com.lielar.domain.dto.LoginResponseDTO;
import com.lielar.domain.dto.JwtResponseDTO;
import com.lielar.domain.dto.RefreshTokenRequestDTO;
import com.lielar.domain.entities.User;
import com.lielar.domain.entities.RefreshToken;
import com.lielar.infrastructure.mappers.UserMapper;
import com.lielar.infrastructure.repositories.UserRepository;
import com.lielar.infrastructure.repositories.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Serviço para operações com usuários
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * Autentica um usuário e retorna JWT
     */
    public Optional<JwtResponseDTO> authenticate(LoginRequestDTO loginRequest) {
        try {
            Optional<User> userOpt = userRepository.findByEmailAndAtivoTrue(loginRequest.getEmail());
            
            if (userOpt.isEmpty()) {
                log.warn("Tentativa de login com email inexistente: {}", loginRequest.getEmail());
                return Optional.empty();
            }

            User user = userOpt.get();
            
            if (!passwordEncoder.matches(loginRequest.getSenha(), user.getSenha())) {
                log.warn("Tentativa de login com senha incorreta para email: {}", loginRequest.getEmail());
                return Optional.empty();
            }

            // Atualiza último login
            user.setUltimoLogin(LocalDateTime.now());
            user.setUpdatedAt();
            userRepository.save(user);

            // Gera tokens JWT
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
            
            // Salva refresh token no banco
            saveRefreshToken(user, refreshToken);
            
            log.info("Usuário autenticado com sucesso: {}", user.getEmail());
            return Optional.of(buildJwtResponse(user, accessToken, refreshToken));
            
        } catch (Exception e) {
            log.error("Erro durante autenticação: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    /**
     * Renova access token usando refresh token
     */
    public Optional<JwtResponseDTO> refreshToken(RefreshTokenRequestDTO refreshRequest) {
        try {
            Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository.findByToken(refreshRequest.getRefreshToken());
            
            if (refreshTokenOpt.isEmpty()) {
                log.warn("Refresh token não encontrado");
                return Optional.empty();
            }

            RefreshToken refreshToken = refreshTokenOpt.get();
            
            if (!refreshToken.isValid()) {
                log.warn("Refresh token inválido ou expirado para usuário: {}", refreshToken.getUserEmail());
                refreshTokenRepository.delete(refreshToken);
                return Optional.empty();
            }

            Optional<User> userOpt = userRepository.findById(refreshToken.getUserId());
            if (userOpt.isEmpty()) {
                log.warn("Usuário não encontrado para refresh token");
                refreshTokenRepository.delete(refreshToken);
                return Optional.empty();
            }

            User user = userOpt.get();
            
            // Gera novos tokens
            String newAccessToken = jwtService.generateAccessToken(user);
            String newRefreshToken = jwtService.generateRefreshToken(user);
            
            // Remove refresh token antigo e salva o novo
            refreshTokenRepository.delete(refreshToken);
            saveRefreshToken(user, newRefreshToken);
            
            log.info("Tokens renovados com sucesso para usuário: {}", user.getEmail());
            return Optional.of(buildJwtResponse(user, newAccessToken, newRefreshToken));
            
        } catch (Exception e) {
            log.error("Erro durante renovação de token: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    /**
     * Faz logout do usuário (revoga refresh token)
     */
    public void logout(String refreshToken) {
        try {
            refreshTokenRepository.findByToken(refreshToken)
                    .ifPresent(refreshTokenRepository::delete);
            log.info("Logout realizado com sucesso");
        } catch (Exception e) {
            log.error("Erro durante logout: {}", e.getMessage(), e);
        }
    }

    /**
     * Salva refresh token no banco
     */
    private void saveRefreshToken(User user, String refreshToken) {
        // Calcula expiração baseada na configuração JWT
        long expirationSeconds = jwtService.getTimeUntilExpiration(refreshToken);
        if (expirationSeconds <= 0) {
            // Se não conseguir calcular, usa o valor padrão da configuração
            expirationSeconds = 604800L; // 7 dias em segundos
        }
        
        RefreshToken token = RefreshToken.builder()
                .id(UUID.randomUUID().toString())
                .token(refreshToken)
                .userId(user.getId())
                .userEmail(user.getEmail())
                .expiresAt(LocalDateTime.now().plusSeconds(expirationSeconds))
                .createdAt(LocalDateTime.now())
                .revoked(false)
                .build();
        
        refreshTokenRepository.save(token);
    }

    /**
     * Constrói resposta JWT
     */
    private JwtResponseDTO buildJwtResponse(User user, String accessToken, String refreshToken) {
        // Calcula tempo de expiração do access token
        long expiresIn = jwtService.getTimeUntilExpiration(accessToken);
        if (expiresIn <= 0) {
            // Se não conseguir calcular, usa o valor padrão da configuração
            expiresIn = 900L; // 15 minutos em segundos
        }
        
        return JwtResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(expiresIn)
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
     * Mapeia permissões do usuário
     */
    private JwtResponseDTO.UserPermissoesDTO mapPermissoes(User.Permissoes permissoes) {
        if (permissoes == null) {
            return null;
        }

        return JwtResponseDTO.UserPermissoesDTO.builder()
                .produtos(permissoes.getProdutos())
                .categorias(permissoes.getCategorias())
                .usuarios(permissoes.getUsuarios())
                .orcamentos(permissoes.getOrcamentos())
                .relatorios(permissoes.getRelatorios())
                .build();
    }

    /**
     * Cria usuário ADMIN padrão se não existir
     */
    public void createDefaultAdminUser() {
        try {
            if (userRepository.existsByEmail("admin@lie-lar.com")) {
                log.info("Usuário ADMIN já existe, pulando criação");
                return;
            }

            User.Permissoes permissoes = User.Permissoes.builder()
                    .produtos(true)
                    .categorias(true)
                    .usuarios(true)
                    .orcamentos(true)
                    .relatorios(true)
                    .build();

            User adminUser = User.builder()
                    .nome("Administrador")
                    .email("admin@lie-lar.com")
                    .senha(passwordEncoder.encode("admin123"))
                    .tipo("admin")
                    .cargo("Administrador do Sistema")
                    .departamento("administrativo")
                    .ativo(true)
                    .permissoes(permissoes)
                    .build();

            adminUser.setCreatedAt();
            userRepository.save(adminUser);
            
            log.info("Usuário ADMIN criado com sucesso: {}", adminUser.getEmail());
            
        } catch (Exception e) {
            log.error("Erro ao criar usuário ADMIN padrão: {}", e.getMessage(), e);
        }
    }



    /**
     * Verifica se um email já está em uso
     */
    public boolean isEmailInUse(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Busca usuário por email
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Busca usuário por ID
     */
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }
}
