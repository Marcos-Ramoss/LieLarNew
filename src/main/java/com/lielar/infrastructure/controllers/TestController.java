package com.lielar.infrastructure.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller de teste para verificar se a aplicação está funcionando
 */
@RestController
@RequestMapping("/api/test")
@Tag(name = "Teste", description = "Endpoints de teste da aplicação")
@CrossOrigin(origins = "*")
public class TestController {

    @GetMapping("/health")
    @Operation(summary = "Verificar saúde da aplicação", description = "Retorna status da aplicação")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now());
        response.put("message", "Lie Lar Backend está funcionando!");
        response.put("version", "1.0.0");
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ping")
    @Operation(summary = "Teste de conectividade", description = "Retorna pong para verificar conectividade")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }

    @GetMapping("/auth-test")
    @Operation(summary = "Teste de autenticação", description = "Testa se o usuário está autenticado e retorna informações do usuário")
    public ResponseEntity<Map<String, Object>> authTest() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        
        Map<String, Object> response = new HashMap<>();
        response.put("authenticated", auth != null && auth.isAuthenticated());
        response.put("username", auth != null ? auth.getName() : "N/A");
        response.put("authorities", auth != null ? auth.getAuthorities().stream()
                .map(Object::toString)
                .collect(Collectors.toList()) : List.of());
        response.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.ok(response);
    }
}
