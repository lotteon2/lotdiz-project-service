package com.lotdiz.projectservice.mapper;

import com.lotdiz.projectservice.dto.request.ProjectInformationForAchievedTargetAmountRequestDto;
import com.lotdiz.projectservice.entity.Project;
import java.util.List;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

  @Named("PIFATARD")
  ProjectInformationForAchievedTargetAmountRequestDto
      getProjectInformationForAchievedTargetAmountRequestDto(Project project);

  @IterableMapping(qualifiedByName = "PIFATARD")
  List<ProjectInformationForAchievedTargetAmountRequestDto>
      getListOfProjectInformationForAchievedTargetAmountRequestDto(List<Project> projects);
}
