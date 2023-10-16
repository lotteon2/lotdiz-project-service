package com.lotdiz.projectservice.repository;

import com.lotdiz.projectservice.dto.ProjectThumbnailImageDto;
import com.lotdiz.projectservice.entity.Project;
import com.lotdiz.projectservice.entity.ProjectImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectImageRepository extends JpaRepository<ProjectImage, Long> {
  List<ProjectImage> findByProject(Project project);

  @Query(
      "select NEW com.lotdiz.projectservice.dto.ProjectThumbnailImageDto(p.project.projectId, p.projectImageUrl) "
          + "from ProjectImage p "
          + "where p.project.projectId in (:projectIds) "
          + "and p.projectImageIsThumbnail= true")
  List<ProjectThumbnailImageDto> findProjectThumbnailImageByProjectId(
      @Param("projectIds") List<Long> projectIds);
}
