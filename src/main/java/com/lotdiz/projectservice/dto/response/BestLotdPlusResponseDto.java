package com.lotdiz.projectservice.dto.response;

import com.lotdiz.projectservice.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class BestLotdPlusResponseDto {

  private Long projectId;
  private String projectName;
  private Long remainingDays;
  private String projectThumbnailImageUrl;
  private String makerName;
  private Long fundingAchievementRate;
  private Long accumulatedFundingAmount;
  private String projectStatus;
  private Boolean isLike;

  public static BestLotdPlusResponseDto toDto(
          Project project,
          String projectThumbnailImageUrl,
          Boolean islike,
          FundingAchievementResultOfProjectResponseDto fundingAchievementResultOfProjectResponseDto) {

    return BestLotdPlusResponseDto.builder()
            .projectId(project.getProjectId())
            .projectName(project.getProjectName())
            .remainingDays(ChronoUnit.DAYS.between(LocalDateTime.now(), project.getProjectDueDate()))
            .projectThumbnailImageUrl(projectThumbnailImageUrl)
            .makerName(project.getMaker().getMakerName())
            .fundingAchievementRate(
                    fundingAchievementResultOfProjectResponseDto.getFundingAchievementRate())
            .accumulatedFundingAmount(
                    fundingAchievementResultOfProjectResponseDto.getAccumulatedFundingAmount())
            .projectStatus(project.getProjectStatus().getMessage())
            .isLike(islike)
            .build();
  }
}
