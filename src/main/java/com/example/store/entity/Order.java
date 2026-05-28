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
        name = "\"order\"",
        indexes = {@Index(name = "idx_order_customer_id", columnList = "customer_id")})
@EqualsAndHashCode(exclude = "products")
@ToString(exclude = "products")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "order_product",
            joinColumns = @JoinColumn(name = "order_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "product_id", nullable = false),
            indexes = {
                @Index(name = "idx_order_product_order", columnList = "order_id"),
                @Index(name = "idx_order_product_product", columnList = "product_id")
            })
    private List<Product> products = new ArrayList<>();
}
