package com.lotdiz.projectservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class FundingAchievementResultOfProjectResponseDto {

  private Long fundingAchievementRate;
  private Long accumulatedFundingAmount;

}
