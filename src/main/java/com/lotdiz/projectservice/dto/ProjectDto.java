package com.lotdiz.projectservice.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProjectDto {
  private Long projectId;
  private String projectName;
  private Long remainingDays;
  private String projectThumbnailImageUrl;
  private String makerName;
  private Long fundingAchievementRate;
  private Long accumulatedFundingAmount;
  private LocalDateTime lotdealDueTime;
  private String projectStatus;
}
