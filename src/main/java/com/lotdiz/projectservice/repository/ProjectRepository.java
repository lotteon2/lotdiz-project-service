package com.lotdiz.projectservice.repository;

import com.lotdiz.projectservice.entity.Project;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectRepository extends JpaRepository<Project, Long> {

  @Query(
      "SELECT p FROM Project p JOIN p.category c WHERE c.categoryName =:categoryName AND p.projectIsAuthorized  =:projectIsAuthorized")
  Optional<Page<Project>> findByCategoryAndProjectIsAuthorized(
      @Param(value = "categoryName") String categoryName,
      @Param(value = "projectIsAuthorized") Boolean projectIsAuthorized,
      Pageable pageable);
}
