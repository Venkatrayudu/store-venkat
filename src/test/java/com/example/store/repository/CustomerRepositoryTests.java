package com.example.store.repository;

import com.example.store.entity.Customer;

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
class CustomerRepositoryTests {

    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setName("John Doe");
    }

    @Test
    void testSaveAndFindCustomer() {
        Customer saved = customerRepository.save(customer);
        assertNotNull(saved.getId());

        Optional<Customer> found = customerRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("John Doe", found.get().getName());
    }

    @Test
    void testSearchByName() {
        customerRepository.save(customer);

        Customer another = new Customer();
        another.setName("Jane Smith");
        customerRepository.save(another);

        List<Customer> results = customerRepository.searchByNameContaining("John");
        assertEquals(1, results.size());
        assertEquals("John Doe", results.get(0).getName());
    }

    @Test
    void testSearchByNameCaseInsensitive() {
        customerRepository.save(customer);

        List<Customer> results = customerRepository.searchByNameContaining("john");
        assertEquals(1, results.size());

        List<Customer> results2 = customerRepository.searchByNameContaining("JOHN");
        assertEquals(1, results2.size());
    }

    @Test
    void testSearchByPartialName() {
        customerRepository.save(customer);

        List<Customer> results = customerRepository.searchByNameContaining("Doe");
        assertEquals(1, results.size());
        assertEquals("John Doe", results.get(0).getName());
    }

    @Test
    void testSearchNoResults() {
        customerRepository.save(customer);

        List<Customer> results = customerRepository.searchByNameContaining("NonExistent");
        assertTrue(results.isEmpty());
    }

    @Test
    void testDeleteCustomer() {
        Customer saved = customerRepository.save(customer);
        Long id = saved.getId();

        customerRepository.delete(saved);

        Optional<Customer> found = customerRepository.findById(id);
        assertTrue(found.isEmpty());
    }
}
