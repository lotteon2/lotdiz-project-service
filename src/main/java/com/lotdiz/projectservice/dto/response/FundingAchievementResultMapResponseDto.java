package com.lotdiz.projectservice.dto.response;

import java.util.Map;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FundingAchievementResultMapResponseDto {
  private Map<String, FundingAchievementResultOfProjectResponseDto>
      fundingAchievementResultOfProjects;

  @Builder
  public FundingAchievementResultMapResponseDto(
      Map<String, FundingAchievementResultOfProjectResponseDto>
          fundingAchievementResultOfProjects) {
    this.fundingAchievementResultOfProjects = fundingAchievementResultOfProjects;
  }
}
