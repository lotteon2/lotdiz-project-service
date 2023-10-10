package com.lotdiz.projectservice.messagequeue;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lotdiz.projectservice.dto.response.TargetAmountAchievedProjectsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendTargetAmountAchievedProjects {
  private final AmazonSQS sqs;
  private final ObjectMapper objectMapper;

  @Value("${cloud.aws.sqs.achieved-target-amount-notification-queue.url}")
  private String url;

  public void sendTargetAmountAchievedProjectsMessageRequest(
      TargetAmountAchievedProjectsDto projectsDtoList) {
    try {
      SendMessageRequest sendMessageRequest =
          new SendMessageRequest(url, objectMapper.writeValueAsString(projectsDtoList));
      sqs.sendMessage(sendMessageRequest);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
