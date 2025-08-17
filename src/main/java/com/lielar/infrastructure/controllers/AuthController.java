package com.lielar.infrastructure.controllers;

import com.lielar.application.services.UserService;
import com.lielar.domain.dto.LoginRequestDTO;
import com.lielar.domain.dto.LoginResponseDTO;
import com.lielar.domain.dto.JwtResponseDTO;
import com.lielar.domain.dto.RefreshTokenRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller para operações de autenticação
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Endpoints para autenticação de usuários")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuário", description = "Realiza login do usuário com email e senha e retorna JWT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
            content = @Content(schema = @Schema(implementation = JwtResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas"),
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    })
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        try {
            log.info("Tentativa de login para email: {}", loginRequest.getEmail());
            
            var jwtResponse = userService.authenticate(loginRequest);
            
            if (jwtResponse.isPresent()) {
                log.info("Login realizado com sucesso para: {}", loginRequest.getEmail());
                return ResponseEntity.ok(jwtResponse.get());
            } else {
                log.warn("Falha na autenticação para email: {}", loginRequest.getEmail());
                
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "Email ou senha inválidos");
                errorResponse.put("error", "UNAUTHORIZED");
                
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }
            
        } catch (Exception e) {
            log.error("Erro durante processo de login: {}", e.getMessage(), e);
            
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Erro interno do servidor");
            errorResponse.put("error", "INTERNAL_SERVER_ERROR");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/refresh")
    @Operation(summary = "Renovar token", description = "Renova access token usando refresh token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Token renovado com sucesso",
            content = @Content(schema = @Schema(implementation = JwtResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Refresh token inválido"),
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    })
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequestDTO refreshRequest) {
        try {
            log.info("Tentativa de renovação de token");
            
            var jwtResponse = userService.refreshToken(refreshRequest);
            
            if (jwtResponse.isPresent()) {
                log.info("Token renovado com sucesso");
                return ResponseEntity.ok(jwtResponse.get());
            } else {
                log.warn("Falha na renovação de token");
                
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "Refresh token inválido ou expirado");
                errorResponse.put("error", "UNAUTHORIZED");
                
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }
            
        } catch (Exception e) {
            log.error("Erro durante renovação de token: {}", e.getMessage(), e);
            
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Erro interno do servidor");
            errorResponse.put("error", "INTERNAL_SERVER_ERROR");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Faz logout do usuário revogando o refresh token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Logout realizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    })
    public ResponseEntity<?> logout(@RequestParam String refreshToken) {
        try {
            log.info("Tentativa de logout");
            
            userService.logout(refreshToken);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Logout realizado com sucesso");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Erro durante logout: {}", e.getMessage(), e);
            
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Erro interno do servidor");
            errorResponse.put("error", "INTERNAL_SERVER_ERROR");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/health")
    @Operation(summary = "Verificar saúde da autenticação", description = "Endpoint para verificar se o serviço de auth está funcionando")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Authentication Service");
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }
}
