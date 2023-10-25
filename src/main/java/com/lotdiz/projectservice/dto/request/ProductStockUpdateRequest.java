package com.lotdiz.projectservice.dto.request;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductStockUpdateRequest {
    private Long productId;
    private Long productFundingQuantity;
}
