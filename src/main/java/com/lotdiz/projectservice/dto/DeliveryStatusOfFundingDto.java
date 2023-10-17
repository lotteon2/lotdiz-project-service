package com.lotdiz.projectservice.dto;

import com.lotdiz.projectservice.entity.DeliveryStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class DeliveryStatusOfFundingDto {
  private Long fundingId;
  private DeliveryStatus deliveryStatus;
}
