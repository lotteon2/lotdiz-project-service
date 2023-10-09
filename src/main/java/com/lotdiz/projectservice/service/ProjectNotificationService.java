package com.lotdiz.projectservice.service;

import com.lotdiz.projectservice.entity.Project;
import com.lotdiz.projectservice.repository.ProjectRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectNotificationService {

  private final ProjectRepository projectRepository;

  public List<Project> getAllByProjectDueDateAfterNow() {
    return projectRepository.findAllByProjectWithMakerDueDateAfter(LocalDateTime.now());
  }
}
