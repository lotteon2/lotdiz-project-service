package com.lotdiz.projectservice.mapper;

import com.lotdiz.projectservice.dto.ProductDto;
import com.lotdiz.projectservice.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper {

  ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

  ProductDto productEntityToProductDto(Product product);
}
