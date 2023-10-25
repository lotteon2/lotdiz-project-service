package com.lotdiz.projectservice.repository;

import com.lotdiz.projectservice.dto.response.GetProjectInfoForLikesResponseDto;
import com.lotdiz.projectservice.dto.response.ProjectAndMakerInfoDto;
import com.lotdiz.projectservice.entity.Maker;
import com.lotdiz.projectservice.entity.Project;
import com.lotdiz.projectservice.entity.ProjectStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectRepository extends JpaRepository<Project, Long> {
  @Query(
      "select p from Project p inner join p.category c where c.categoryName =:categoryName "
          + "and p.projectIsAuthorized  =:projectIsAuthorized")
  Page<Project> findByCategoryAndProjectIsAuthorized(
      String categoryName, Boolean projectIsAuthorized, Pageable pageable);

  @Query(
      "select p from Project p "
          + "join fetch p.maker m join fetch p.lotdeal l "
          + "where p.projectDueDate < :now")
  List<Project> findAllByProjectWithMakerDueDateAfter(LocalDateTime now);

  @Query(
      "select new com.lotdiz.projectservice.dto.response.GetProjectInfoForLikesResponseDto(p.projectName, i.projectImageUrl, m.makerName, p.projectDueDate) "
          + "from Project p join fetch Maker m on p.maker.makerId = m.makerId join fetch ProjectImage i on p.projectId = i.project.projectId "
          + "and i.projectImageIsThumbnail = true where p.projectId in :projectIds")
  List<GetProjectInfoForLikesResponseDto> findProjectInfoForLikes(List<Long> projectIds);

  Page<Project> findByProjectTagAndProjectIsAuthorized(
      String projectTag, Boolean projectIsAuthorized, Pageable pageable);

  @Query(
      "select p from Project p join SupportSignature s on p.projectId = s.project.projectId "
          + "left join Lotdeal l on p.projectId = l.project.projectId "
          + "where l.lotdealDueTime < :now or l.lotdealStartTime > :now or l.lotdealId is null "
          + "group by p.projectId order by count(s.project) desc")
  Page<Project> findBestLotdPlus(LocalDateTime now, Pageable pageable);

  @Query(
      "select new com.lotdiz.projectservice.dto.response.ProjectAndMakerInfoDto(p.projectId, p.projectName, "
          + "p.projectTargetAmount, p.projectStatus, i.projectImageUrl, m.makerName, p.projectDueDate) "
          + "from Project p join Maker m on p.maker.makerId = m.makerId join ProjectImage i on p.projectId = i.project.projectId "
          + "and i.projectImageIsThumbnail = true where p.projectId in :projectIds")
  List<ProjectAndMakerInfoDto> findMakerProject(List<Long> projectIds);

  @Modifying
  @Query(
      "update Project p set p.projectStatus = :projectStatus where p.projectDueDate < :now and p.projectId in :projectIds")
  int updateProjectStatusDueDateAfter(
      @Param("projectStatus") ProjectStatus projectStatus,
      @Param("now") LocalDateTime now,
      @Param("projectIds") List<Long> projectIds);

  @Query("select p from Project p where p.maker = :maker")
  Page<Project> findByRegisteredProjects(@Param("maker") Maker maker, Pageable pageable);
}
