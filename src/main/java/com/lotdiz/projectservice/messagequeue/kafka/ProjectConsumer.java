package com.lotdiz.projectservice.messagequeue.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lotdiz.projectservice.dto.request.AuthorizedProjectRequestDto;
import com.lotdiz.projectservice.entity.Project;
import com.lotdiz.projectservice.entity.ProjectStatus;
import com.lotdiz.projectservice.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectConsumer {

  private final ObjectMapper mapper;
  private final ProjectRepository projectRepository;

  @KafkaListener(topics = "auth-project")
  @Transactional
  public void receiveAuthorizedProject(String message, Acknowledgment ack) {
    try {
      AuthorizedProjectRequestDto authorizedProjectRequestDto =
          mapper.readValue(message, AuthorizedProjectRequestDto.class);

      Project project = projectRepository.findById(authorizedProjectRequestDto.getProjectId()).get();
      project.setProjectIsAuthorized(true);
      project.setProjectStatus(ProjectStatus.PROCESSING);

      ack.acknowledge();
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
