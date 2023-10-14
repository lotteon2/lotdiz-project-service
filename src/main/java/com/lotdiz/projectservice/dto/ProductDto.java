package com.lotdiz.projectservice.dto;

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
}
