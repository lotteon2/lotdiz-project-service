package com.lotdiz.projectservice.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TargetAmountAchievedProjectsDto {

  private Long fundingId;
  private Long memberId;
  private String projectName;
  private String memberName;
}
