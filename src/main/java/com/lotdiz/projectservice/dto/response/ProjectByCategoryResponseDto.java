package com.lotdiz.projectservice.dto.response;

import com.lotdiz.projectservice.entity.Lotdeal;
import com.lotdiz.projectservice.entity.Project;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ProjectByCategoryResponseDto {

  private Long projectId;
  private String projectName;
  private Long remainingDays;
  private String projectThumbnailImageUrl;
  private String makerName;
  private Long fundingAchievementRate;
  private Long accumulatedFundingAmount;
  private LocalDateTime lotdealDueTime;
  private String projectStatus;
  private Boolean isLike;

  public static ProjectByCategoryResponseDto toDto(
      Project project,
      String projectThumbnailImageUrl,
      Boolean islike,
      FundingAchievementResultOfProjectResponseDto fundingAchievementResultOfProjectResponseDto,
      Lotdeal lotdeal) {

    LocalDateTime lotdealDueTime = null;
    if (lotdeal != null) {
      lotdealDueTime = lotdeal.getLotdealDueTime();
    }

    return ProjectByCategoryResponseDto.builder()
        .projectId(project.getProjectId())
        .projectName(project.getProjectName())
        .remainingDays(ChronoUnit.DAYS.between(LocalDateTime.now(), project.getProjectDueDate()))
        .projectThumbnailImageUrl(projectThumbnailImageUrl)
        .makerName(project.getMaker().getMakerName())
        .fundingAchievementRate(
            fundingAchievementResultOfProjectResponseDto.getFundingAchievementRate())
        .accumulatedFundingAmount(
            fundingAchievementResultOfProjectResponseDto.getAccumulatedFundingAmount())
        .lotdealDueTime(lotdealDueTime)
        .projectStatus(project.getProjectStatus().getMessage())
        .isLike(islike)
        .build();
  }
}
