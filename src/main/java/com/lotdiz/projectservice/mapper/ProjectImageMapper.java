package com.lotdiz.projectservice.mapper;

import com.lotdiz.projectservice.dto.ProjectImageDto;
import com.lotdiz.projectservice.entity.ProjectImage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProjectImageMapper {

    ProjectImageMapper INSTANCE = Mappers.getMapper(ProjectImageMapper.class);

    ProjectImageDto projectImageEntityToProjectImageDto(ProjectImage projectImage);

}
