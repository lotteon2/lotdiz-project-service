package com.lotdiz.projectservice.dto;

import com.lotdiz.projectservice.entity.Project;
import com.lotdiz.projectservice.entity.ProjectImage;
import com.lotdiz.projectservice.mapper.ProjectImageMapper;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ProjectImageDto {

  private Long projectImageId;
  private String projectImageUrl;
  private Boolean projectImageIsThumbnail;

  public static ProjectImageDto fromProjectImageEntity(ProjectImage projectImage) {
    return ProjectImageMapper.INSTANCE.projectImageEntityToProjectImageDto(projectImage);
  }
}
