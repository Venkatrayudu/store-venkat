package com.example.store.mapper;

import com.example.store.dto.ProductDTO;
import com.example.store.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "orderIds", expression = "java(product.getOrders().stream().map(order -> order.getId()).collect(java.util.stream.Collectors.toList()))")
    ProductDTO productToProductDTO(Product product);

    @Named("withoutOrders")
    @Mapping(target = "orderIds", expression = "java(java.util.Collections.emptyList())")
    ProductDTO productToProductDTOWithoutOrders(Product product);

    List<ProductDTO> productsToProductDTOs(List<Product> products);

    @Mapping(target = "orders", ignore = true)
    Product productDTOToProduct(ProductDTO productDTO);
}

