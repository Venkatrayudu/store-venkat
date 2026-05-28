package com.example.store.controller;

import com.example.store.dto.CustomerDTO;
import com.example.store.entity.Customer;
import com.example.store.exception.ResourceNotFoundException;
import com.example.store.mapper.CustomerMapper;
import com.example.store.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    /**
     * Get all customers.
     */
    @GetMapping
    @Transactional(readOnly = true)
    public List<CustomerDTO> getAllCustomers() {
        log.debug("Fetching all customers");
        return customerMapper.customersToCustomerDTOs(customerRepository.findAll());
    }

    /**
     * Get a customer by ID.
     *
     * @param id the customer ID
     * @return the customer DTO
     * @throws ResourceNotFoundException if customer not found
     */
    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public CustomerDTO getCustomerById(@PathVariable Long id) {
        log.debug("Fetching customer with id: {}", id);
        return customerRepository.findById(id)
                .map(customerMapper::customerToCustomerDTO)
                .orElseThrow(() -> {
                    log.error("Customer not found with id: {}", id);
                    return new ResourceNotFoundException("Customer not found with id: " + id);
                });
    }

    /**
     * Search customers by name using substring match (case-insensitive).
     * Searches across all words in the customer name.
     *
     * @param query the search query
     * @return list of matching customers
     */
    @GetMapping("/search")
    @Transactional(readOnly = true)
    public List<CustomerDTO> searchCustomers(@RequestParam String query) {
        log.debug("Searching customers with query: {}", query);
        List<Customer> customers = customerRepository.searchByNameContaining(query);
        return customerMapper.customersToCustomerDTOs(customers);
    }

    /**
     * Create a new customer.
     *
     * @param customer the customer to create
     * @return the created customer DTO
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public CustomerDTO createCustomer(@RequestBody Customer customer) {
        log.info("Creating new customer with name: {}", customer.getName());
        return customerMapper.customerToCustomerDTO(customerRepository.save(customer));
    }
}
