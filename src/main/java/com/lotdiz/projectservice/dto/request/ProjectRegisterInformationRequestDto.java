package com.lotdiz.projectservice.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectRegisterInformationRequestDto {
  private final String projectName;
  private final String projectThumbnailImageUrl;
  private final List<String> projectImages;
  private final String projectDescription;
  private final String projectStoryImageUrl;
  private final String categoryName;
  private final String projectTag;
  private final Long projectTargetAmount;
  private final LocalDateTime projectDueDate;

  private final boolean isLotdeal;
  private MakerRegisterRequestDto maker;
  private List<ProductInfoForProjectRequestDto> products;
}
