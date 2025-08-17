package com.lielar.infrastructure.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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
}
