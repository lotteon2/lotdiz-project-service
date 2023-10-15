package com.lotdiz.projectservice.service;

import com.lotdiz.projectservice.dto.request.ProductStockUpdateRequest;
import com.lotdiz.projectservice.dto.response.ProductStockCheckResponse;
import com.lotdiz.projectservice.entity.Product;
import com.lotdiz.projectservice.exception.ProductEntityNotFoundException;
import com.lotdiz.projectservice.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FundingClientService {

  private final ProductRepository productRepository;

  @Transactional(readOnly = true)
  public List<ProductStockCheckResponse> checkStockQuantityExceed(List<Long> productIds) {

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
}
