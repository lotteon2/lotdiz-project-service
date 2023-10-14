package com.lotdiz.projectservice.dto.request;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateProjectRequestDto {

  private Long projectId;
  private String projectName;
  private String categoryName;
  private String makerName;
  private LocalDateTime projectDueDate;
  private Boolean projectIsAuthorized;
  private LocalDateTime createdAt;
}
