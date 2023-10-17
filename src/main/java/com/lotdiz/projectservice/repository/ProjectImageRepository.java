package com.lotdiz.projectservice.repository;

import com.lotdiz.projectservice.entity.Project;
import com.lotdiz.projectservice.entity.ProjectImage;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProjectImageRepository extends JpaRepository<ProjectImage, Long> {
  List<ProjectImage> findByProject(Project project);

  @Query("select i.projectImageUrl from ProjectImage i where i.project.projectId = :#{#project.projectId} and i.projectImageIsThumbnail = :projectImageIsThumbnail")
  String findProjectImageByProjectAndAndProjectImageIsThumbnail(Project project, Boolean projectImageIsThumbnail);
}
