package com.lotdiz.projectservice.repository;

import com.lotdiz.projectservice.dto.LotdealDueDateDto;
import com.lotdiz.projectservice.entity.Lotdeal;
import com.lotdiz.projectservice.entity.Project;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LotdealRepository extends JpaRepository<Lotdeal, Long> {

  @Query(
      "select l from Lotdeal l where l.project.projectId = :#{#project.projectId} AND l.lotdealStartTime <= :now AND l.lotdealDueTime > :now")
  Lotdeal findByProjectAndLotdealing(
      @Param("project") Project project, @Param("now") LocalDateTime now);

  @Query(
      "SELECT NEW com.lotdiz.projectservice.dto.LotdealDueDateDto(l.project.projectId, l.lotdealDueTime) FROM Lotdeal l WHERE l.project.projectId IN (:projectIds)")
  List<LotdealDueDateDto> findLotdealDueDateByProjectId(@Param("projectIds") List<Long> projectIds);

  @Query(
      value =
          "select l from Lotdeal l join fetch l.project p where p.projectIsAuthorized = :projectIsAuthorized "
              + "and (l.lotdealStartTime <= :now and l.lotdealDueTime > :now)",
      countQuery =
          "select count(l) from Lotdeal l join l.project p where p.projectIsAuthorized = :projectIsAuthorized "
              + "and (l.lotdealStartTime <= :now and l.lotdealDueTime > :now)" )
  Page<Lotdeal> findAllLotdealing(
      LocalDateTime now, Boolean projectIsAuthorized, Pageable pageable);
}
