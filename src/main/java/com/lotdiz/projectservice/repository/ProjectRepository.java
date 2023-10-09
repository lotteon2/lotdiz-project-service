package com.lotdiz.projectservice.repository;

import com.lotdiz.projectservice.entity.Project;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProjectRepository extends JpaRepository<Project, Long> {

  @Query("select p from Project p " + "join fetch p.maker m " + "where p.projectDueDate < :now")
  List<Project> findAllByProjectWithMakerDueDateAfter(LocalDateTime now);
}
