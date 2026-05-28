package com.example.store.mapper;

import com.example.store.dto.CustomerDTO;
import com.example.store.dto.CustomerOrderDTO;
import com.example.store.entity.Customer;
import com.example.store.entity.Order;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "orders", expression = "java(mapOrdersToCustomerOrderDTOs(customer.getOrders()))")
    CustomerDTO customerToCustomerDTO(Customer customer);

    List<CustomerDTO> customersToCustomerDTOs(List<Customer> customers);

    default List<CustomerOrderDTO> mapOrdersToCustomerOrderDTOs(List<Order> orders) {
        if (orders == null) {
            return java.util.Collections.emptyList();
        }
        return orders.stream()
                .map(o -> {
                    CustomerOrderDTO dto = new CustomerOrderDTO();
                    dto.setId(o.getId());
                    dto.setDescription(o.getDescription());
                    dto.setProductIds(o.getProducts().stream().map(p -> p.getId()).collect(Collectors.toList()));
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
