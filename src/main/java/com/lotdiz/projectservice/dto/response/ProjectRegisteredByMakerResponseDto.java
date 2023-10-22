package com.lotdiz.projectservice.dto.response;

import com.lotdiz.projectservice.dto.ProjectDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectRegisteredByMakerResponseDto {
  private final List<ProjectDto> projects;
  private final int totalPage;
}
