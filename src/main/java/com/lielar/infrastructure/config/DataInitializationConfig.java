package com.lielar.infrastructure.config;

import com.lielar.application.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Configuração para inicialização de dados padrão
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializationConfig implements CommandLineRunner {

    private final UserService userService;

    @Override
    public void run(String... args) throws Exception {
        log.info("🚀 Iniciando configuração de dados padrão...");
        
        try {
            // Cria usuário ADMIN padrão
            userService.createDefaultAdminUser();
            
            log.info("✅ Configuração de dados padrão concluída com sucesso!");
            log.info("📧 Usuário ADMIN: admin@lie-lar.com");
            log.info("🔑 Senha: admin123");
            
        } catch (Exception e) {
            log.error("❌ Erro durante inicialização de dados padrão: {}", e.getMessage(), e);
        }
    }
}
