package com.example.store.controller;

import com.example.store.dto.ProductDTO;
import com.example.store.entity.Order;
import com.example.store.entity.Product;
import com.example.store.exception.ResourceNotFoundException;
import com.example.store.mapper.ProductMapper;
import com.example.store.repository.OrderRepository;
import com.example.store.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ProductMapper productMapper;

    /** Get all products with their associated order IDs. */
    @GetMapping
    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts() {
        log.debug("Fetching all products");
        List<Product> products = productRepository.findAll();
        return productMapper.productsToProductDTOs(products);
    }

    /**
     * Get a product by ID with associated order IDs.
     *
     * @param id the product ID
     * @return the product DTO
     * @throws ResourceNotFoundException if product not found
     */
    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ProductDTO getProductById(@PathVariable Long id) {
        log.debug("Fetching product with id: {}", id);
        Product product = productRepository.findById(id).orElseThrow(() -> {
            log.error("Product not found with id: {}", id);
            return new ResourceNotFoundException("Product not found with id: " + id);
        });
        return productMapper.productToProductDTO(product);
    }

    /**
     * Search products by description (case-insensitive substring match).
     *
     * @param query the search query
     * @return list of matching product DTOs
     */
    @GetMapping("/search")
    @Transactional(readOnly = true)
    public List<ProductDTO> searchProducts(@RequestParam String query) {
        log.debug("Searching products with query: {}", query);
        List<Product> products = productRepository.findByDescriptionContainingIgnoreCase(query);
        return productMapper.productsToProductDTOs(products);
    }

    /**
     * Create a new product.
     *
     * @param product the product to create
     * @return the created product DTO
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ProductDTO createProduct(@RequestBody ProductDTO productDTO) {
        log.info("Creating new product with description: {}", productDTO.getDescription());
        Product product = productMapper.productDTOToProduct(productDTO);
        product.setId(null);
        Product savedProduct = productRepository.save(product);

        if (productDTO.getOrderIds() != null && !productDTO.getOrderIds().isEmpty()) {
            for (Long orderId : productDTO.getOrderIds()) {
                Order order = orderRepository
                        .findById(orderId)
                        .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
                order.getProducts().add(savedProduct);
                orderRepository.save(order);
            }
        }

        return productMapper.productToProductDTO(
                productRepository.findById(savedProduct.getId()).orElseThrow());
    }

    /**
     * Update an existing product.
     *
     * @param id the product ID
     * @param productDetails the updated product details
     * @return the updated product DTO
     * @throws ResourceNotFoundException if product not found
     */
    @PutMapping("/{id}")
    @Transactional
    public ProductDTO updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        log.debug("Updating product with id: {}", id);
        Product product = productRepository.findById(id).orElseThrow(() -> {
            log.error("Product not found with id: {}", id);
            return new ResourceNotFoundException("Product not found with id: " + id);
        });

        if (productDetails.getDescription() != null) {
            product.setDescription(productDetails.getDescription());
        }

        Product updatedProduct = productRepository.save(product);
        log.info("Product updated successfully with id: {}", id);
        return productMapper.productToProductDTO(updatedProduct);
    }

    /**
     * Delete a product by ID.
     *
     * @param id the product ID
     * @throws ResourceNotFoundException if product not found
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void deleteProduct(@PathVariable Long id) {
        log.debug("Deleting product with id: {}", id);
        Product product = productRepository.findById(id).orElseThrow(() -> {
            log.error("Product not found with id: {}", id);
            return new ResourceNotFoundException("Product not found with id: " + id);
        });

        productRepository.delete(product);
        log.info("Product deleted successfully with id: {}", id);
    }
}
