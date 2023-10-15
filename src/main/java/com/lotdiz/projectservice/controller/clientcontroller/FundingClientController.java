package com.lotdiz.projectservice.controller.clientcontroller;

import com.lotdiz.projectservice.dto.request.ProductStockUpdateRequest;
import com.lotdiz.projectservice.dto.response.ProductStockCheckResponse;
import com.lotdiz.projectservice.service.FundingClientService;
import com.lotdiz.projectservice.utils.SuccessResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FundingClientController {

  private final FundingClientService fundingClientService;

  @PostMapping ("/projects/check-stock-quantity-exceed")
  SuccessResponse<List<ProductStockCheckResponse>> getStockQuantityCheckExceed(
      @RequestBody List<Long> productIds) {

    List<ProductStockCheckResponse> productStockCheckResponses =
        fundingClientService.checkStockQuantityExceed(productIds);

    return SuccessResponse.<List<ProductStockCheckResponse>>builder()
        .code(String.valueOf(HttpStatus.OK.value()))
        .message(HttpStatus.OK.name())
        .detail("project-service : 상품 재고 확인 요청 성공")
        .data(productStockCheckResponses)
        .build();
  }

  @PostMapping("/projects/update-stock-quantity")
  SuccessResponse updateStockQuantity(
      @RequestBody List<ProductStockUpdateRequest> updateProductStockQuantityRequestDto) {

      fundingClientService.updateProductStockQuantity(updateProductStockQuantityRequestDto);

    return SuccessResponse.builder()
        .code(String.valueOf(HttpStatus.OK.value()))
        .message(HttpStatus.OK.name())
        .detail("project-service : 상품 재고 수정 요청 성공")
        .build();
  }
}
