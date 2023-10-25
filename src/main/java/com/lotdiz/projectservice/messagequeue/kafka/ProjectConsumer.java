package com.lotdiz.projectservice.messagequeue.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lotdiz.projectservice.dto.request.AuthorizedProjectRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectConsumer {

  private final ObjectMapper mapper;

  @KafkaListener(topics = "auth-project")
  public void receiveAuthorizedProject(String message, Acknowledgment ack) {
    try {
      AuthorizedProjectRequestDto authorizedProjectRequestDto =
          mapper.readValue(message, AuthorizedProjectRequestDto.class);

      // TODO: DB에서 projectId 조회 후 승인 상태 업데이트

      ack.acknowledge();
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
