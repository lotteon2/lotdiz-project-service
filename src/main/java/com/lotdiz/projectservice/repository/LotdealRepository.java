package com.lotdiz.projectservice.repository;

import com.lotdiz.projectservice.entity.Lotdeal;
import com.lotdiz.projectservice.entity.Project;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LotdealRepository extends JpaRepository<Lotdeal, Long> {

  @Query(
      "SELECT l FROM Lotdeal l WHERE l.project.projectId = :#{#project.projectId} AND l.lotdealStartTime <= :now AND l.lotdealDueTime > :now")
  Lotdeal findByProjectAndLotdealing(
      @Param("project") Project project, @Param("now") LocalDateTime now);

  @Query("SELECT l FROM Lotdeal l JOIN l.project p WHERE p.projectIsAuthorized = :projectIsAuthorized AND l.lotdealStartTime <= :now AND l.lotdealDueTime > :now")
  Page<Lotdeal> findAllLotdealing(
          LocalDateTime now, Boolean projectIsAuthorized, Pageable pageable
  );
}
