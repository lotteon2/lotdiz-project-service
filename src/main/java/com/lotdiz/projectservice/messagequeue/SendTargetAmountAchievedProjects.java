package com.lotdiz.projectservice.messagequeue;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lotdiz.projectservice.dto.response.TargetAmountAchievedProjectsDto;
import java.util.List;
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
      List<TargetAmountAchievedProjectsDto> projectsDtoList) throws JsonProcessingException {
    SendMessageRequest sendMessageRequest =
        new SendMessageRequest(url, objectMapper.writeValueAsString(projectsDtoList));
    SendMessageResult sendMessageResult = sqs.sendMessage(sendMessageRequest);
    log.info(
        "send target amount achieved projects data to sqs success {}",
        sendMessageResult.toString());
  }
}
