package com.lotdiz.projectservice.dto.response;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class GetProjectInfoForLikesResponseDto {

  private String projectName;
  private String projectThumbnailImageUrl;
  private String makerName;
  private Long remainingDays;

  public GetProjectInfoForLikesResponseDto(
      String projectName, String projectThumbnailImageUrl, String makerName, LocalDateTime projectDueDate) {
    this.projectName = projectName;
    this.projectThumbnailImageUrl = projectThumbnailImageUrl;
    this.makerName = makerName;
    this.remainingDays = ChronoUnit.DAYS.between(LocalDateTime.now(), projectDueDate);
  }
}
