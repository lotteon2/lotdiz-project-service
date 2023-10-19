package com.lotdiz.projectservice.entity;

import com.lotdiz.projectservice.entity.common.BaseEntity;
import javax.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Setter
@Entity
@Table(name = "product")
public class Product extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "product_id")
  private Long productId;

  @ManyToOne(targetEntity = Project.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "project_id")
  private Project project;

  @Column(name = "product_name", nullable = false)
  private String productName;

  @Column(name = "product_description", nullable = false)
  private String productDescription;

  @Column(name = "product_price", nullable = false)
  private Long productPrice;

  @Column(name = "product_registered_stock_quantity", nullable = false)
  private Long productRegisteredStockQuantity;

  @Column(name = "product_current_stock_quantity", nullable = false)
  private Long productCurrentStockQuantity;
}
