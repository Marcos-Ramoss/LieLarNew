package com.lielar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal da aplicação Lie Lar
 * Sistema de Gestão de Materiais Eletrônicos e Construção
 */
@SpringBootApplication
public class LieLarApplication {

    public static void main(String[] args) {
        SpringApplication.run(LieLarApplication.class, args);
        System.out.println("🚀 Lie Lar Backend iniciado com sucesso!");
        System.out.println("📖 Swagger UI disponível em: http://localhost:8080/swagger-ui.html");
        System.out.println("🔗 API Docs disponível em: http://localhost:8080/api-docs");
    }
}
