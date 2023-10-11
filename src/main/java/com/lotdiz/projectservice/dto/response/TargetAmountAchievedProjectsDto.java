package com.lotdiz.projectservice.dto.response;

import java.util.List;
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
  private String projectName;
  private List<Long> memberIds;
}
