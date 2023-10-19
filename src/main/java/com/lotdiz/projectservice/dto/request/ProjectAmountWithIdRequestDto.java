package com.lotdiz.projectservice.dto.request;

import com.lotdiz.projectservice.dto.ProjectAmountWithIdDto;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProjectAmountWithIdRequestDto {
  private List<ProjectAmountWithIdDto> projectAmountWithIdDtos;
}
