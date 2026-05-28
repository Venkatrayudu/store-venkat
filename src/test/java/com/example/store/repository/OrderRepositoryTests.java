package com.example.store.repository;

import com.example.store.entity.Customer;
import com.example.store.entity.Order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class OrderRepositoryTests {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setName("Test Customer");
        customer = customerRepository.save(customer);
    }

    @Test
    void testSaveAndFindOrder() {
        Order order = new Order();
        order.setDescription("Test Order");
        order.setCustomer(customer);

        Order saved = orderRepository.save(order);
        assertNotNull(saved.getId());

        Optional<Order> found = orderRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Test Order", found.get().getDescription());
    }

    @Test
    void testFindAllWithDetails() {
        Order order = new Order();
        order.setDescription("Order With Details");
        order.setCustomer(customer);
        orderRepository.save(order);

        List<Order> orders = orderRepository.findAllWithDetails();
        assertFalse(orders.isEmpty());
        assertNotNull(orders.get(0).getCustomer());
    }

    @Test
    void testFindByIdWithDetails() {
        Order order = new Order();
        order.setDescription("Detailed Order");
        order.setCustomer(customer);
        Order saved = orderRepository.save(order);

        Optional<Order> found = orderRepository.findByIdWithDetails(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Detailed Order", found.get().getDescription());
        assertNotNull(found.get().getCustomer());
    }

    @Test
    void testFindByIdWithDetailsNotFound() {
        Optional<Order> found = orderRepository.findByIdWithDetails(999L);
        assertTrue(found.isEmpty());
    }

    @Test
    void testDeleteOrder() {
        Order order = new Order();
        order.setDescription("To Delete");
        order.setCustomer(customer);
        Order saved = orderRepository.save(order);
        Long id = saved.getId();

        orderRepository.delete(saved);

        assertTrue(orderRepository.findById(id).isEmpty());
    }
}
