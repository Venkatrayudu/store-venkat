package com.example.store.mapper;

import com.example.store.dto.OrderCustomerDTO;
import com.example.store.dto.OrderDTO;
import com.example.store.dto.ProductDTO;
import com.example.store.entity.Customer;
import com.example.store.entity.Order;
import com.example.store.entity.Product;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "products", expression = "java(mapProductsToProductDTOs(order.getProducts()))")
    OrderDTO orderToOrderDTO(Order order);

    @Named("withoutProducts")
    @Mapping(target = "products", expression = "java(java.util.Collections.emptyList())")
    OrderDTO orderToOrderDTOWithoutProducts(Order order);

    List<OrderDTO> ordersToOrderDTOs(List<Order> orders);

    OrderCustomerDTO orderToOrderCustomerDTO(Customer customer);

    default List<ProductDTO> mapProductsToProductDTOs(List<Product> products) {
        if (products == null) {
            return java.util.Collections.emptyList();
        }
        return products.stream()
                .map(p -> {
                    ProductDTO dto = new ProductDTO();
                    dto.setId(p.getId());
                    dto.setDescription(p.getDescription());
                    dto.setOrderIds(p.getOrders().stream().map(Order::getId).collect(Collectors.toList()));
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
