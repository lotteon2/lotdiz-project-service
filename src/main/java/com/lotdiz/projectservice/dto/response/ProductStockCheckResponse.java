package com.lotdiz.projectservice.dto.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductStockCheckResponse {
    private Long productId;
    private Long productCurrentStockQuantity;

    public ProductStockCheckResponse(long productId, long productCurrentStockQuantity) {
        this.productId = productId;
        this.productCurrentStockQuantity = productCurrentStockQuantity;
    }
}
