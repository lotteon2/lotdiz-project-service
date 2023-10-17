package com.lotdiz.projectservice.service;

import com.lotdiz.projectservice.dto.request.GetProjectProductInfoRequestDto;
import com.lotdiz.projectservice.dto.request.ProductStockUpdateRequest;
import com.lotdiz.projectservice.dto.response.GetProductInfoDto;
import com.lotdiz.projectservice.dto.response.GetProjectProductInfoResponseDto;
import com.lotdiz.projectservice.dto.response.ProductStockCheckResponseDto;
import com.lotdiz.projectservice.dto.response.ProjectAndMakerInfoDto;
import com.lotdiz.projectservice.entity.Product;
import com.lotdiz.projectservice.entity.Project;
import com.lotdiz.projectservice.exception.ProductEntityNotFoundException;
import com.lotdiz.projectservice.exception.ProjectEntityNotFoundException;
import com.lotdiz.projectservice.repository.ProductRepository;
import com.lotdiz.projectservice.repository.ProjectRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FundingClientService {

  private final ProductRepository productRepository;
  private final ProjectRepository projectRepository;

  @Transactional(readOnly = true)
  public List<ProductStockCheckResponseDto> checkStockQuantityExceed(List<Long> productIds) {

    return productRepository.findProductsByIds(productIds);
  }

  @Transactional
  public void updateProductStockQuantity(
      List<ProductStockUpdateRequest> updateProductStockQuantityRequestDtos) {

    for (ProductStockUpdateRequest p : updateProductStockQuantityRequestDtos) {
      Product product =
          productRepository
              .findById(p.getProductId())
              .orElseThrow(ProductEntityNotFoundException::new);
      product.setProductCurrentStockQuantity(
          product.getProductCurrentStockQuantity() - p.getProductFundingQuantity());
      productRepository.save(product);
    }
  }

  @Transactional(readOnly = true)
  public List<ProjectAndMakerInfoDto> getProjectMakerInfo(List<Long> projectIds) {

    return projectRepository.findMakerProject(projectIds);
  }

  @Transactional(readOnly = true)
  public GetProjectProductInfoResponseDto getProjectProductInfo(
      GetProjectProductInfoRequestDto getProjectProductInfoRequestDto) {

    Project project =
        projectRepository
            .findById(getProjectProductInfoRequestDto.getProjectId())
            .orElseThrow(ProjectEntityNotFoundException::new);

    List<GetProductInfoDto> product =
        productRepository.findByProductIds(getProjectProductInfoRequestDto.getProductIds());

    return GetProjectProductInfoResponseDto.builder()
        .projectId(project.getProjectId())
        .projectDescription(project.getProjectDescription())
        .products(product)
        .build();
  }
}
