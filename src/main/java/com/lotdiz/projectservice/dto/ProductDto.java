package com.lotdiz.projectservice.dto;

import com.lotdiz.projectservice.entity.Product;
import com.lotdiz.projectservice.mapper.ProductMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ProductDto {

  private Long productId;
  private String productName;
  private String productDescription;
  private Long productPrice;
  private Long productRegisteredStockQuantity;
  private Long productCurrentStockQuantity;

  public static ProductDto fromProductEntity(Product product) {
    return ProductMapper.INSTANCE.productEntityToProductDto(product);
  }
}
