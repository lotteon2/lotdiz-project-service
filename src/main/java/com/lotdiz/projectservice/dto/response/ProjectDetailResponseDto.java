package com.lotdiz.projectservice.dto.response;

import com.lotdiz.projectservice.dto.ProductDto;
import com.lotdiz.projectservice.dto.ProjectImageDto;
import com.lotdiz.projectservice.entity.Lotdeal;
import com.lotdiz.projectservice.entity.Project;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ProjectDetailResponseDto {

  private Long projectId;
  private String projectName;
  private String projectDescription;
  private Long remainingDays;
  private String makerName;
  private String categoryName;
  private String projectTag;
  private Long numberOfBuyers;
  private Long numberOfSupporter;
  private Long numberOfLikes;
  private Long fundingAchievementRate;
  private Long accumulatedFundingAmount;
  private String projectStoryImageUrl;
  private List<ProjectImageDto> projectImages;
  private LocalDateTime lotdealDueTime;
  private String projectStatus;
  private List<ProductDto> products;

  public static ProjectDetailResponseDto fromProjectEntity(
      Project project,
      List<ProjectImageDto> projectImages,
      List<ProductDto> products,
      Long numberOfSupporter,
      LocalDateTime lotdealDueTime) {

    return ProjectDetailResponseDto.builder()
        .projectId(project.getProjectId())
        .projectName(project.getProjectName())
        .projectDescription(project.getProjectDescription())
        .remainingDays(ChronoUnit.DAYS.between(LocalDateTime.now(), project.getProjectDueDate()))
        .makerName(project.getMaker().getMakerName())
        .categoryName(project.getCategory().getCategoryName())
        .projectTag(project.getProjectTag())
        .numberOfBuyers(0L) // TODO
        .numberOfLikes(0L) // TODO
        .numberOfSupporter(numberOfSupporter)
        .fundingAchievementRate(0L) // TODO
        .accumulatedFundingAmount(0L) // TODO
        .projectStoryImageUrl(project.getProjectStoryImageUrl())
        .projectImages(projectImages)
        .lotdealDueTime(lotdealDueTime)
        .projectStatus(project.getProjectStatus().getMessage())
        .products(products)
        .build();
  }
}