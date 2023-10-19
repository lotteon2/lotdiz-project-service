package com.lotdiz.projectservice.dto.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductStockCheckResponseDto {
    private Long productId;
    private Long productCurrentStockQuantity;

    public ProductStockCheckResponseDto(long productId, long productCurrentStockQuantity) {
        this.productId = productId;
        this.productCurrentStockQuantity = productCurrentStockQuantity;
    }
}
