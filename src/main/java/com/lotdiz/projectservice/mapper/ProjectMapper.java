package com.lotdiz.projectservice.mapper;

import com.lotdiz.projectservice.dto.ProjectDto;
import com.lotdiz.projectservice.dto.request.CreateProjectRequestDto;
import com.lotdiz.projectservice.dto.request.GetTargetAmountCheckExceedRequestDto;
import com.lotdiz.projectservice.dto.request.ProjectInformationForAchievedTargetAmountRequestDto;
import com.lotdiz.projectservice.entity.Project;
import java.util.List;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

  @Named("PIFATARD")
  ProjectInformationForAchievedTargetAmountRequestDto
      getProjectInformationForAchievedTargetAmountRequestDto(Project project);

  @IterableMapping(qualifiedByName = "PIFATARD")
  List<ProjectInformationForAchievedTargetAmountRequestDto>
      getListOfProjectInformationForAchievedTargetAmountRequestDto(List<Project> projects);

  CreateProjectRequestDto projectEntityToCreateProjectRequestDto(Project project);
  @Named("PFRP")
  ProjectDto getProjectDtoForRegisteredProject(Project project);

  @IterableMapping(qualifiedByName = "PFRP")
  List<ProjectDto> getProjectDtoForRegisteredProjectList(List<Project> projects);

  @Named("TACERD")
  @Mapping(target = "makerMemberId", source = "maker.memberId")
  GetTargetAmountCheckExceedRequestDto projectToGetTargetAmountCheckExceedRequestDto(
      Project project);

  @IterableMapping(qualifiedByName = "TACERD")
  List<GetTargetAmountCheckExceedRequestDto> projectsToGetTargetAmountCheckExceedRequestDtos(
      List<Project> projects);
}
