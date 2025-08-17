package com.lielar.application.services;

import com.lielar.domain.entities.Product;
import com.lielar.domain.entities.Category;
import com.lielar.domain.dto.ProductRequestDTO;
import com.lielar.domain.dto.ProductResponseDTO;
import com.lielar.infrastructure.repositories.ProductRepository;
import com.lielar.infrastructure.repositories.CategoryRepository;
import com.lielar.infrastructure.mappers.ProductMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Serviço de negócio para operações com produtos
 */
@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, 
                         CategoryRepository categoryRepository,
                         ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    /**
     * Cria um novo produto
     */
    public ProductResponseDTO createProduct(ProductRequestDTO requestDTO) {
        // Valida se já existe produto com o mesmo código
        if (productRepository.existsByCodigo(requestDTO.getCodigo())) {
            throw new RuntimeException("Já existe um produto com o código: " + requestDTO.getCodigo());
        }

        // Busca a categoria
        Category category = categoryRepository.findByIdAndAtivoTrue(requestDTO.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada ou inativa"));

        Product product = productMapper.toEntity(requestDTO, category);
        Product savedProduct = productRepository.save(product);
        
        return productMapper.toResponseDTO(savedProduct);
    }

    /**
     * Busca produto por ID
     */
    @Transactional(readOnly = true)
    public Optional<ProductResponseDTO> findProductById(String id) {
        return productRepository.findByIdAndAtivoTrue(id)
                .map(productMapper::toResponseDTO);
    }

    /**
     * Busca produto por código
     */
    @Transactional(readOnly = true)
    public Optional<ProductResponseDTO> findProductByCode(String codigo) {
        return productRepository.findByCodigo(codigo)
                .filter(Product::isActive)
                .map(productMapper::toResponseDTO);
    }

    /**
     * Lista todos os produtos ativos
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findAllActiveProducts() {
        List<Product> products = productRepository.findByAtivoTrue();
        return productMapper.toResponseDTOList(products);
    }

    /**
     * Lista produtos com paginação
     */
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> findProductsWithPagination(Pageable pageable) {
        Page<Product> products = productRepository.findByAtivoTrue(pageable);
        return products.map(productMapper::toResponseDTO);
    }

    /**
     * Busca produtos por categoria
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findProductsByCategory(String categoryId) {
        List<Product> products = productRepository.findByCategoriaIdAndAtivoTrue(categoryId);
        return productMapper.toResponseDTOList(products);
    }

    /**
     * Busca produtos em destaque
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findFeaturedProducts() {
        List<Product> products = productRepository.findByDestaqueTrueAndAtivoTrue();
        return productMapper.toResponseDTOList(products);
    }

    /**
     * Busca produtos por nome
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findProductsByName(String nome) {
        List<Product> products = productRepository.findByNomeContainingIgnoreCaseAndAtivoTrue(nome);
        return productMapper.toResponseDTOList(products);
    }

    /**
     * Busca produtos por código
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findProductsByCode(String codigo) {
        List<Product> products = productRepository.findByCodigoContainingIgnoreCaseAndAtivoTrue(codigo);
        return productMapper.toResponseDTOList(products);
    }

    /**
     * Busca produtos com estoque baixo
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findProductsWithLowStock(Integer quantidade) {
        List<Product> products = productRepository.findByEstoqueLessThanAndAtivoTrue(quantidade);
        return productMapper.toResponseDTOList(products);
    }

    /**
     * Atualiza um produto existente
     */
    public Optional<ProductResponseDTO> updateProduct(String id, ProductRequestDTO requestDTO) {
        return productRepository.findByIdAndAtivoTrue(id)
                .map(product -> {
                    // Verifica se o novo código já existe em outro produto
                    if (!product.getCodigo().equals(requestDTO.getCodigo()) && 
                        productRepository.existsByCodigo(requestDTO.getCodigo())) {
                        throw new RuntimeException("Já existe um produto com o código: " + requestDTO.getCodigo());
                    }
                    
                    // Busca a categoria se foi alterada
                    Category category = null;
                    if (requestDTO.getCategoriaId() != null) {
                        category = categoryRepository.findByIdAndAtivoTrue(requestDTO.getCategoriaId())
                                .orElseThrow(() -> new RuntimeException("Categoria não encontrada ou inativa"));
                    }
                    
                    productMapper.updateEntityFromDTO(product, requestDTO, category);
                    Product updatedProduct = productRepository.save(product);
                    return productMapper.toResponseDTO(updatedProduct);
                });
    }

    /**
     * Desativa um produto (soft delete)
     */
    public Optional<ProductResponseDTO> deactivateProduct(String id) {
        return productRepository.findByIdAndAtivoTrue(id)
                .map(product -> {
                    product.setAtivo(false);
                    product.setUpdatedAt();
                    Product deactivatedProduct = productRepository.save(product);
                    return productMapper.toResponseDTO(deactivatedProduct);
                });
    }

    /**
     * Ativa um produto
     */
    public Optional<ProductResponseDTO> activateProduct(String id) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setAtivo(true);
                    product.setUpdatedAt();
                    Product activatedProduct = productRepository.save(product);
                    return productMapper.toResponseDTO(activatedProduct);
                });
    }

    /**
     * Atualiza estoque de um produto
     */
    public Optional<ProductResponseDTO> updateStock(String id, Integer newStock) {
        if (newStock < 0) {
            throw new RuntimeException("Estoque não pode ser negativo");
        }
        
        return productRepository.findByIdAndAtivoTrue(id)
                .map(product -> {
                    product.setEstoque(newStock);
                    product.setUpdatedAt();
                    Product updatedProduct = productRepository.save(product);
                    return productMapper.toResponseDTO(updatedProduct);
                });
    }

    /**
     * Verifica se produto existe e está ativo
     */
    @Transactional(readOnly = true)
    public boolean existsAndActive(String id) {
        return productRepository.existsByIdAndAtivoTrue(id);
    }

    /**
     * Conta produtos ativos
     */
    @Transactional(readOnly = true)
    public long countActiveProducts() {
        return productRepository.countByAtivoTrue();
    }
}
