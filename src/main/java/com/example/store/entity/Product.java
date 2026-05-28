package com.example.store.entity;

import jakarta.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(
        name = "product",
        indexes = {@Index(name = "idx_product_description", columnList = "description")})
@EqualsAndHashCode(exclude = "orders")
@ToString(exclude = "orders")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String description;

    @ManyToMany(mappedBy = "products", fetch = jakarta.persistence.FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();
}
