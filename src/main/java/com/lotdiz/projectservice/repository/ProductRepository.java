package com.lotdiz.projectservice.repository;

import com.lotdiz.projectservice.dto.response.GetProductInfoDto;
import com.lotdiz.projectservice.dto.response.ProductStockCheckResponseDto;
import com.lotdiz.projectservice.entity.Product;
import com.lotdiz.projectservice.entity.Project;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {
  List<Product> findByProject(Project project);

  @Query(
      "select new com.lotdiz.projectservice.dto.response.ProductStockCheckResponseDto(p.productId, p.productCurrentStockQuantity) from Product p where p.productId in :productIds")
  List<ProductStockCheckResponseDto> findProductsByIds(List<Long> productIds);

  @Query(
      "select new com.lotdiz.projectservice.dto.response.GetProductInfoDto(p.productId, p.productName, p.productDescription) from Product p where p.productId in :productIds")
  List<GetProductInfoDto> findByProductIds(List<Long> productIds);
}
