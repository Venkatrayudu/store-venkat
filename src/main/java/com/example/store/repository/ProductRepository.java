package com.example.store.repository;

import com.example.store.entity.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.orders")
    List<Product> findAllWithDetails();

    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.orders WHERE p.id = :id")
    Optional<Product> findByIdWithDetails(Long id);

    @Query(
            "SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.orders WHERE LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Product> findByDescriptionContainingIgnoreCase(String searchTerm);
}
