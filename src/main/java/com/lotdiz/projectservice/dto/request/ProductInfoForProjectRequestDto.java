package com.lotdiz.projectservice.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductInfoForProjectRequestDto {
    private final String productName;
    private final Long productPrice;
    private final String productDescription;
    private final Long productRegisteredStockQuantity;
  private final Long productCurrentStockQuantity;
}
