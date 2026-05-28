package com.example.store.repository;

import com.example.store.entity.Product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testSaveAndFindProduct() {
        Product product = new Product();
        product.setDescription("Test Product");

        Product saved = productRepository.save(product);
        assertNotNull(saved.getId());

        Optional<Product> found = productRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Test Product", found.get().getDescription());
    }

    @Test
    void testSearchByDescription() {
        Product product = new Product();
        product.setDescription("Wireless Keyboard");
        productRepository.save(product);

        List<Product> results = productRepository.findByDescriptionContainingIgnoreCase("wireless");
        assertEquals(1, results.size());
        assertEquals("Wireless Keyboard", results.get(0).getDescription());
    }

    @Test
    void testSearchCaseInsensitive() {
        Product product = new Product();
        product.setDescription("USB Hub");
        productRepository.save(product);

        assertEquals(1, productRepository.findByDescriptionContainingIgnoreCase("usb").size());
        assertEquals(1, productRepository.findByDescriptionContainingIgnoreCase("USB").size());
    }

    @Test
    void testSearchNoResults() {
        List<Product> results = productRepository.findByDescriptionContainingIgnoreCase("NonExistent");
        assertTrue(results.isEmpty());
    }

    @Test
    void testDeleteProduct() {
        Product product = new Product();
        product.setDescription("To Delete");
        Product saved = productRepository.save(product);
        Long id = saved.getId();

        productRepository.delete(saved);

        assertTrue(productRepository.findById(id).isEmpty());
    }
}
