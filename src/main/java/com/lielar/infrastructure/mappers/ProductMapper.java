package com.lielar.infrastructure.mappers;

import com.lielar.domain.entities.Product;
import com.lielar.domain.entities.Category;
import com.lielar.domain.dto.ProductRequestDTO;
import com.lielar.domain.dto.ProductResponseDTO;
import com.lielar.domain.dto.CategoryResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapeador para conversão entre Product e DTOs
 */
@Component
public class ProductMapper {

    private final CategoryMapper categoryMapper;

    public ProductMapper(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    /**
     * Converte ProductRequestDTO para Product
     */
    public Product toEntity(ProductRequestDTO dto, Category category) {
        Product product = Product.builder()
                .nome(dto.getNome())
                .descricao(dto.getDescricao())
                .preco(dto.getPreco())
                .codigo(dto.getCodigo())
                .categoria(category)
                .marca(dto.getMarca())
                .modelo(dto.getModelo())
                .estoque(dto.getEstoque() != null ? dto.getEstoque() : 0)
                .unidade(dto.getUnidade() != null ? dto.getUnidade() : "un")
                .peso(dto.getPeso())
                .dimensoes(dto.getDimensoes())
                .ativo(dto.getAtivo() != null ? dto.getAtivo() : true)
                .destaque(dto.getDestaque() != null ? dto.getDestaque() : false)
                .imagens(dto.getImagens())
                .build();

        product.setCreatedAt();
        
        return product;
    }

    /**
     * Converte Product para ProductResponseDTO
     */
    public ProductResponseDTO toResponseDTO(Product product) {
        CategoryResponseDTO categoriaDTO = null;
        if (product.getCategoria() != null) {
            categoriaDTO = categoryMapper.toResponseDTO(product.getCategoria());
        }

        return ProductResponseDTO.builder()
                .id(product.getId())
                .nome(product.getNome())
                .descricao(product.getDescricao())
                .preco(product.getPreco())
                .codigo(product.getCodigo())
                .categoria(categoriaDTO)
                .marca(product.getMarca())
                .modelo(product.getModelo())
                .estoque(product.getEstoque())
                .unidade(product.getUnidade())
                .peso(product.getPeso())
                .dimensoes(product.getDimensoes())
                .ativo(product.getAtivo())
                .destaque(product.getDestaque())
                .imagens(product.getImagens())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    /**
     * Converte lista de Product para lista de ProductResponseDTO
     */
    public List<ProductResponseDTO> toResponseDTOList(List<Product> products) {
        return products.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Atualiza Product com dados do ProductRequestDTO
     */
    public void updateEntityFromDTO(Product product, ProductRequestDTO dto, Category category) {
        if (dto.getNome() != null) {
            product.setNome(dto.getNome());
        }
        if (dto.getDescricao() != null) {
            product.setDescricao(dto.getDescricao());
        }
        if (dto.getPreco() != null) {
            product.setPreco(dto.getPreco());
        }
        if (dto.getCodigo() != null) {
            product.setCodigo(dto.getCodigo());
        }
        if (category != null) {
            product.setCategoria(category);
        }
        if (dto.getMarca() != null) {
            product.setMarca(dto.getMarca());
        }
        if (dto.getModelo() != null) {
            product.setModelo(dto.getModelo());
        }
        if (dto.getEstoque() != null) {
            product.setEstoque(dto.getEstoque());
        }
        if (dto.getUnidade() != null) {
            product.setUnidade(dto.getUnidade());
        }
        if (dto.getPeso() != null) {
            product.setPeso(dto.getPeso());
        }
        if (dto.getDimensoes() != null) {
            product.setDimensoes(dto.getDimensoes());
        }
        if (dto.getAtivo() != null) {
            product.setAtivo(dto.getAtivo());
        }
        if (dto.getDestaque() != null) {
            product.setDestaque(dto.getDestaque());
        }
        if (dto.getImagens() != null) {
            product.setImagens(dto.getImagens());
        }
        
        product.setUpdatedAt();
    }
}
