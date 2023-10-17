package com.lotdiz.projectservice.mapper;

import com.lotdiz.projectservice.dto.ProductDto;
import com.lotdiz.projectservice.entity.Product;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

  @Named("product")
  ProductDto productEntityToProductDto(Product product);

  @IterableMapping(qualifiedByName = "product")
  List<ProductDto> productEntityToProductDtoList(List<Product> products);
}
