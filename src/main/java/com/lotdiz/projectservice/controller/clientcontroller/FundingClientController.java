package com.lotdiz.projectservice.controller.clientcontroller;

import com.lotdiz.projectservice.dto.request.GetProjectProductInfoRequestDto;
import com.lotdiz.projectservice.dto.request.ProductStockUpdateRequest;
import com.lotdiz.projectservice.dto.response.GetProjectProductInfoResponseDto;
import com.lotdiz.projectservice.dto.response.ProductStockCheckResponseDto;
import com.lotdiz.projectservice.dto.response.ProjectAndMakerInfoDto;
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

  @PostMapping("/projects/check-stock-quantity-exceed")
  SuccessResponse<List<ProductStockCheckResponseDto>> getStockQuantityCheckExceed(
      @RequestBody List<Long> productIds) {

    List<ProductStockCheckResponseDto> productStockCheckResponses =
        fundingClientService.checkStockQuantityExceed(productIds);

    return SuccessResponse.<List<ProductStockCheckResponseDto>>builder()
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

  @GetMapping("/project/get-project-maker-info")
  SuccessResponse<List<ProjectAndMakerInfoDto>> getProjectMakerInfo(
      @RequestParam List<Long> projectIds) {

    List<ProjectAndMakerInfoDto> projectAndMakerInfos =
        fundingClientService.getProjectMakerInfo(projectIds);

    return SuccessResponse.<List<ProjectAndMakerInfoDto>>builder()
        .code(String.valueOf(HttpStatus.OK.value()))
        .message(HttpStatus.OK.name())
        .detail("project-service : 프로젝트, 메이커 정보 조회 요청 성공")
        .data(projectAndMakerInfos)
        .build();
  }

  @PostMapping("/projects/get-project-product-info")
  SuccessResponse<GetProjectProductInfoResponseDto> getProjectProductInfo(
      @RequestBody GetProjectProductInfoRequestDto getProjectProductInfoRequestDto) {

    GetProjectProductInfoResponseDto getProjectProductInfoResponseDto =
        fundingClientService.getProjectProductInfo(getProjectProductInfoRequestDto);

    return SuccessResponse.<GetProjectProductInfoResponseDto>builder()
        .code(String.valueOf(HttpStatus.OK.value()))
        .message(HttpStatus.OK.name())
        .detail("project-service : 프로젝트, 상품 정보 조회 요청 성공")
        .data(getProjectProductInfoResponseDto)
        .build();
  }
}
