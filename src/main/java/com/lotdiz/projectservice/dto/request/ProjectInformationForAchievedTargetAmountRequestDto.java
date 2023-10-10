package com.lotdiz.projectservice.dto.request;

import com.lotdiz.projectservice.entity.Project;
import com.lotdiz.projectservice.mapper.ProjectMapper;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectInformationForAchievedTargetAmountRequestDto {

  private Long projectId;
  private Long memberId;
  private String projectName;
  private Long projectTargetAmount;

  public static List<ProjectInformationForAchievedTargetAmountRequestDto> fromEntity(
      List<Project> projects) {
    return ProjectMapper.INSTANCE.getListOfProjectInformationForAchievedTargetAmountRequestDto(
        projects);
  }
}
