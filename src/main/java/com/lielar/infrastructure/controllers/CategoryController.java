package com.lielar.infrastructure.controllers;

import com.lielar.application.services.CategoryService;
import com.lielar.domain.dto.CategoryRequestDTO;
import com.lielar.domain.dto.CategoryResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Controller REST para operações com categorias
 */
@RestController
@RequestMapping("/api/categorias")
@Tag(name = "Categorias", description = "API para gerenciamento de categorias de produtos")
@CrossOrigin(origins = "*")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @Operation(summary = "Criar nova categoria", description = "Cria uma nova categoria de produtos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "409", description = "Categoria já existe")
    })
    public ResponseEntity<CategoryResponseDTO> createCategory(
            @Valid @RequestBody CategoryRequestDTO requestDTO) {
        try {
            CategoryResponseDTO category = categoryService.createCategory(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(category);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar categoria por ID", description = "Retorna uma categoria específica pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoria encontrada"),
        @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    public ResponseEntity<CategoryResponseDTO> getCategoryById(
            @Parameter(description = "ID da categoria") @PathVariable String id) {
        return categoryService.findCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Buscar categoria por slug", description = "Retorna uma categoria específica pelo slug")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoria encontrada"),
        @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    public ResponseEntity<CategoryResponseDTO> getCategoryBySlug(
            @Parameter(description = "Slug da categoria") @PathVariable String slug) {
        return categoryService.findCategoryBySlug(slug)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Listar categorias", description = "Retorna todas as categorias ativas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de categorias retornada com sucesso")
    })
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        List<CategoryResponseDTO> categories = categoryService.findAllActiveCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/busca")
    @Operation(summary = "Buscar categorias por nome", description = "Busca categorias que contenham o nome especificado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categorias encontradas")
    })
    public ResponseEntity<List<CategoryResponseDTO>> searchCategoriesByName(
            @Parameter(description = "Nome para busca") @RequestParam String nome) {
        List<CategoryResponseDTO> categories = categoryService.findCategoriesByName(nome);
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar categoria", description = "Atualiza uma categoria existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Categoria não encontrada"),
        @ApiResponse(responseCode = "409", description = "Nome já existe em outra categoria")
    })
    public ResponseEntity<CategoryResponseDTO> updateCategory(
            @Parameter(description = "ID da categoria") @PathVariable String id,
            @Valid @RequestBody CategoryRequestDTO requestDTO) {
        return categoryService.updateCategory(id, requestDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desativar categoria", description = "Desativa uma categoria (soft delete)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoria desativada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    public ResponseEntity<CategoryResponseDTO> deactivateCategory(
            @Parameter(description = "ID da categoria") @PathVariable String id) {
        return categoryService.deactivateCategory(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/ativar")
    @Operation(summary = "Ativar categoria", description = "Reativa uma categoria desativada")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoria ativada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    public ResponseEntity<CategoryResponseDTO> activateCategory(
            @Parameter(description = "ID da categoria") @PathVariable String id) {
        return categoryService.activateCategory(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/count")
    @Operation(summary = "Contar categorias ativas", description = "Retorna o número total de categorias ativas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contagem retornada com sucesso")
    })
    public ResponseEntity<Long> countActiveCategories() {
        long count = categoryService.countActiveCategories();
        return ResponseEntity.ok(count);
    }
}
