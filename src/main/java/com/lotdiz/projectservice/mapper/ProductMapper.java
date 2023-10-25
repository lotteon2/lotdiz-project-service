package com.lotdiz.projectservice.mapper;

import com.lotdiz.projectservice.dto.ProductDto;
import com.lotdiz.projectservice.dto.ProductInformationForRegisteredProjectDto;
import com.lotdiz.projectservice.entity.Product;
import java.util.List;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ProductMapper {

  @Named("product")
  ProductDto productEntityToProductDto(Product product);

  @IterableMapping(qualifiedByName = "product")
  List<ProductDto> productEntityToProductDtoList(List<Product> products);

  @Named("PIFRP")
  ProductInformationForRegisteredProjectDto
      productEntityToProductInformationForRegisteredProjectDto(Product product);

  @IterableMapping(qualifiedByName = "PIFRP")
  List<ProductInformationForRegisteredProjectDto>
      productEntityToProductInformationForRegisteredProjectToList(List<Product> products);
}
