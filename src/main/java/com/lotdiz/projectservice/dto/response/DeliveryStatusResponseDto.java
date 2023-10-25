package com.lotdiz.projectservice.dto.response;

import com.lotdiz.projectservice.dto.DeliveryStatusOfFundingDto;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class DeliveryStatusResponseDto {
  private List<DeliveryStatusOfFundingDto> deliveryStatusOfFundingDtos;
}
