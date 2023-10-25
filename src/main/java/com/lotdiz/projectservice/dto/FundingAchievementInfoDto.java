package com.lotdiz.projectservice.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FundingAchievementInfoDto {
  private String fundingAchievementRate;
  private String accumulatedFundingAmount;
}
