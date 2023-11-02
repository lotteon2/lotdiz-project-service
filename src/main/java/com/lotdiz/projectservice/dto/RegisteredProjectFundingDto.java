package com.lotdiz.projectservice.dto;

import com.lotdiz.projectservice.entity.DeliveryStatus;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RegisteredProjectFundingDto {
  private String supporterName;
  private LocalDateTime fundingDate;
  private Long totalFundingAmount;
  private String deliveryStatus;
}
