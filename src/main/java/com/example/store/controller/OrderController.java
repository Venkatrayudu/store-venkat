package com.example.store.controller;

import com.example.store.dto.OrderDTO;
import com.example.store.entity.Order;
import com.example.store.exception.ResourceNotFoundException;
import com.example.store.mapper.OrderMapper;
import com.example.store.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    /**
     * Get all orders with their associated customers and products.
     * Uses eager loading via JOIN FETCH to optimize performance.
     */
    @GetMapping
    @Transactional(readOnly = true)
    public List<OrderDTO> getAllOrders() {
        log.debug("Fetching all orders");
        return orderMapper.ordersToOrderDTOs(orderRepository.findAllWithDetails());
    }

    /**
     * Get a specific order by ID with its customer and products.
     *
     * @param id the order ID
     * @return the order DTO
     * @throws ResourceNotFoundException if order not found
     */
    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public OrderDTO getOrderById(@PathVariable Long id) {
        log.debug("Fetching order with id: {}", id);
        return orderRepository.findByIdWithDetails(id)
                .map(orderMapper::orderToOrderDTO)
                .orElseThrow(() -> {
                    log.error("Order not found with id: {}", id);
                    return new ResourceNotFoundException("Order not found with id: " + id);
                });
    }

    /**
     * Create a new order.
     *
     * @param order the order to create
     * @return the created order DTO
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public OrderDTO createOrder(@RequestBody Order order) {
        log.info("Creating new order with description: {}", order.getDescription());
        Order savedOrder = orderRepository.save(order);
        return orderMapper.orderToOrderDTO(savedOrder);
    }

    /**
     * Update an existing order.
     *
     * @param id the order ID
     * @param orderDetails the updated order details
     * @return the updated order DTO
     * @throws ResourceNotFoundException if order not found
     */
    @PutMapping("/{id}")
    @Transactional
    public OrderDTO updateOrder(@PathVariable Long id, @RequestBody Order orderDetails) {
        log.debug("Updating order with id: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Order not found with id: {}", id);
                    return new ResourceNotFoundException("Order not found with id: " + id);
                });

        if (orderDetails.getDescription() != null) {
            order.setDescription(orderDetails.getDescription());
        }

        if (orderDetails.getProducts() != null && !orderDetails.getProducts().isEmpty()) {
            order.setProducts(orderDetails.getProducts());
        }

        Order updatedOrder = orderRepository.save(order);
        log.info("Order updated successfully with id: {}", id);
        return orderMapper.orderToOrderDTO(updatedOrder);
    }

    /**
     * Delete an order by ID.
     *
     * @param id the order ID
     * @throws ResourceNotFoundException if order not found
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void deleteOrder(@PathVariable Long id) {
        log.debug("Deleting order with id: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Order not found with id: {}", id);
                    return new ResourceNotFoundException("Order not found with id: " + id);
                });

        orderRepository.delete(order);
        log.info("Order deleted successfully with id: {}", id);
    }
}
