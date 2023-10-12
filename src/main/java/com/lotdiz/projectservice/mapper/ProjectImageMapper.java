package com.lotdiz.projectservice.mapper;

import com.lotdiz.projectservice.dto.ProjectImageDto;
import com.lotdiz.projectservice.entity.ProjectImage;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectImageMapper {
    @Named("projectImage")
    ProjectImageDto projectImageEntityToProjectImageDto(ProjectImage projectImage);

    @IterableMapping(qualifiedByName = "projectImage")
    List<ProjectImageDto> projectImageEntityToProjectImageDtoList(List<ProjectImage> projectImage);
}
