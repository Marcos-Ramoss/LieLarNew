package com.lielar.infrastructure.controllers;

import com.lielar.application.services.ProductService;
import com.lielar.domain.dto.ProductRequestDTO;
import com.lielar.domain.dto.ProductResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Controller REST para operações com produtos
 */
@RestController
@RequestMapping("/api/produtos")
@Tag(name = "Produtos", description = "API para gerenciamento de produtos")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @Operation(summary = "Criar novo produto", description = "Cria um novo produto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "409", description = "Produto já existe")
    })
    public ResponseEntity<ProductResponseDTO> createProduct(
            @Valid @RequestBody ProductRequestDTO requestDTO) {
        try {
            ProductResponseDTO product = productService.createProduct(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(product);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID", description = "Retorna um produto específico pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produto encontrado"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ProductResponseDTO> getProductById(
            @Parameter(description = "ID do produto") @PathVariable String id) {
        return productService.findProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/codigo/{codigo}")
    @Operation(summary = "Buscar produto por código", description = "Retorna um produto específico pelo código")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produto encontrado"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ProductResponseDTO> getProductByCode(
            @Parameter(description = "Código do produto") @PathVariable String codigo) {
        return productService.findProductByCode(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Listar produtos", description = "Retorna todos os produtos ativos com paginação")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso")
    })
    public ResponseEntity<Page<ProductResponseDTO>> getAllProducts(
            @Parameter(description = "Número da página (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductResponseDTO> products = productService.findProductsWithPagination(pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/ativos")
    @Operation(summary = "Listar produtos ativos", description = "Retorna todos os produtos ativos sem paginação")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso")
    })
    public ResponseEntity<List<ProductResponseDTO>> getAllActiveProducts() {
        List<ProductResponseDTO> products = productService.findAllActiveProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/categoria/{categoryId}")
    @Operation(summary = "Listar produtos por categoria", description = "Retorna produtos de uma categoria específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produtos da categoria retornados com sucesso")
    })
    public ResponseEntity<List<ProductResponseDTO>> getProductsByCategory(
            @Parameter(description = "ID da categoria") @PathVariable String categoryId) {
        List<ProductResponseDTO> products = productService.findProductsByCategory(categoryId);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/destaque")
    @Operation(summary = "Listar produtos em destaque", description = "Retorna produtos marcados como destaque")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produtos em destaque retornados com sucesso")
    })
    public ResponseEntity<List<ProductResponseDTO>> getFeaturedProducts() {
        List<ProductResponseDTO> products = productService.findFeaturedProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/busca/nome")
    @Operation(summary = "Buscar produtos por nome", description = "Busca produtos que contenham o nome especificado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produtos encontrados")
    })
    public ResponseEntity<List<ProductResponseDTO>> searchProductsByName(
            @Parameter(description = "Nome para busca") @RequestParam String nome) {
        List<ProductResponseDTO> products = productService.findProductsByName(nome);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/busca/codigo")
    @Operation(summary = "Buscar produtos por código", description = "Busca produtos que contenham o código especificado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produtos encontrados")
    })
    public ResponseEntity<List<ProductResponseDTO>> searchProductsByCode(
            @Parameter(description = "Código para busca") @RequestParam String codigo) {
        List<ProductResponseDTO> products = productService.findProductsByCode(codigo);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/estoque-baixo")
    @Operation(summary = "Listar produtos com estoque baixo", description = "Retorna produtos com estoque menor que a quantidade especificada")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produtos com estoque baixo retornados com sucesso")
    })
    public ResponseEntity<List<ProductResponseDTO>> getProductsWithLowStock(
            @Parameter(description = "Quantidade limite de estoque") @RequestParam(defaultValue = "10") Integer quantidade) {
        List<ProductResponseDTO> products = productService.findProductsWithLowStock(quantidade);
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar produto", description = "Atualiza um produto existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
        @ApiResponse(responseCode = "409", description = "Código já existe em outro produto")
    })
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @Parameter(description = "ID do produto") @PathVariable String id,
            @Valid @RequestBody ProductRequestDTO requestDTO) {
        return productService.updateProduct(id, requestDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/estoque")
    @Operation(summary = "Atualizar estoque", description = "Atualiza o estoque de um produto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estoque atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Estoque inválido"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ProductResponseDTO> updateStock(
            @Parameter(description = "ID do produto") @PathVariable String id,
            @Parameter(description = "Novo estoque") @RequestParam Integer estoque) {
        return productService.updateStock(id, estoque)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desativar produto", description = "Desativa um produto (soft delete)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produto desativado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ProductResponseDTO> deactivateProduct(
            @Parameter(description = "ID do produto") @PathVariable String id) {
        return productService.deactivateProduct(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/ativar")
    @Operation(summary = "Ativar produto", description = "Reativa um produto desativado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produto ativado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ProductResponseDTO> activateProduct(
            @Parameter(description = "ID do produto") @PathVariable String id) {
        return productService.activateProduct(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/count")
    @Operation(summary = "Contar produtos ativos", description = "Retorna o número total de produtos ativos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contagem retornada com sucesso")
    })
    public ResponseEntity<Long> countActiveProducts() {
        long count = productService.countActiveProducts();
        return ResponseEntity.ok(count);
    }
}
