package com.lotdiz.projectservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class GetProductInfoDto {

    private Long productId;
    private String productName;
    private String productDescription;

    public GetProductInfoDto(long productId, String productName, String productDescription) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
    }
}
