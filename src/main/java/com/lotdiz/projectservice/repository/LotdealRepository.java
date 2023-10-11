package com.lotdiz.projectservice.repository;

import com.lotdiz.projectservice.entity.Lotdeal;
import com.lotdiz.projectservice.entity.Project;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LotdealRepository extends JpaRepository<Lotdeal, Long> {

  @Query(
      "SELECT l FROM Lotdeal l WHERE l.project.projectId = :#{#project.projectId} AND l.lotdealStartTime <= :now AND l.lotdealDueTime > :now")
  Optional<Lotdeal> findByProjectAndLotdealing(
      @Param("project") Project project, @Param("now") LocalDateTime now);
}
