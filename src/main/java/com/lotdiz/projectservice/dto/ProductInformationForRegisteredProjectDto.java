package com.lotdiz.projectservice.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductInformationForRegisteredProjectDto {
  private final String productName;
  private final Long productCurrentStockQuantity;
  private final Long productPrice;
}
