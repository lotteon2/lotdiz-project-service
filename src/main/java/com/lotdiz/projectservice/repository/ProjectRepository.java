package com.lotdiz.projectservice.repository;

import com.lotdiz.projectservice.entity.Project;
import com.lotdiz.projectservice.entity.ProjectStatus;

import java.util.Optional;
import java.util.List;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ProjectRepository extends JpaRepository<Project, Long> {

  @Query(
      "SELECT p FROM Project p JOIN p.category c WHERE c.categoryName =:categoryName AND p.projectIsAuthorized  =:projectIsAuthorized")
  Page<Project> findByCategoryAndProjectIsAuthorized(
      @Param(value = "categoryName") String categoryName,
      @Param(value = "projectIsAuthorized") Boolean projectIsAuthorized,
      Pageable pageable);
  
  @Query("select p from Project p " + "join fetch p.maker m " + "where p.projectDueDate < :now")
  List<Project> findAllByProjectWithMakerDueDateAfter(LocalDateTime now);

  Page<Project> findByProjectTagAndProjectIsAuthorized(String projectTag, Boolean projectIsAuthorized, Pageable pageable);

  @Modifying
  @Query(
      "update Project p set p.projectStatus = :projectStatus where p.projectDueDate < :now and p.projectId in :projectIds")
  int updateProjectStatusDueDateAfter(
      @Param("projectStatus") ProjectStatus projectStatus,
      @Param("now") LocalDateTime now,
      @Param("projectIds") List<Long> projectIds);
  
}
