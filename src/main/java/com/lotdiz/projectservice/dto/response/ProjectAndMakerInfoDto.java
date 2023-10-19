package com.lotdiz.projectservice.dto.response;

import com.lotdiz.projectservice.entity.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ProjectAndMakerInfoDto {

  private Long projectId;
  private String projectName;
  private Long projectTargetAmount;
  private String projectStatus;
  private String projectImageUrl;
  private String makerName;
  private Long remainingDays;

  public ProjectAndMakerInfoDto(
      long projectId,
      String projectName,
      long projectTargetAmount,
      ProjectStatus projectStatus,
      String projectImageUrl,
      String makerName,
      LocalDateTime projectDueDate) {
    this.projectId = projectId;
    this.projectName = projectName;
    this.projectTargetAmount = projectTargetAmount;
    this.projectStatus = projectStatus.getMessage();
    this.projectImageUrl = projectImageUrl;
    this.makerName = makerName;
    this.remainingDays =  ChronoUnit.DAYS.between(LocalDateTime.now(), projectDueDate);
  }
}
