package com.lotdiz.projectservice.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lotdiz.projectservice.client.FundingServiceClient;
import com.lotdiz.projectservice.dto.request.GetTargetAmountCheckExceedRequestDto;
import com.lotdiz.projectservice.dto.response.GetTargetAmountCheckExceedResponseDto;
import com.lotdiz.projectservice.entity.Project;
import com.lotdiz.projectservice.exception.FundingServiceClientOutOfServiceException;
import com.lotdiz.projectservice.service.ProjectNotificationService;
import com.lotdiz.projectservice.sns.ProjectNotificationEventPublisher;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.listener.Acknowledgment;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectSqsListener {

  @Value("${cloud.aws.sns.project-due-date-notification-topic.name}")
  private String SNS_DESTINATION_NAME;

  private final ObjectMapper mapper;
  private final FundingServiceClient fundingServiceClient;
  private final ProjectNotificationService projectNotificationService;
  private final ProjectNotificationEventPublisher projectNotificationEventPublisher;
  private final CircuitBreakerFactory circuitBreakerFactory;

  @SqsListener(
      value = "${cloud.aws.sqs.project-due-date-event-queue.name}",
      deletionPolicy = SqsMessageDeletionPolicy.NEVER)
  public void createProjectDueDateNotificationQueue(
      @Payload String message, @Headers Map<String, String> headers, Acknowledgment ack) {
    log.info("receive ProjectDueDateEvent message={}", message);

    // 1. 마감된 프로젝트 조회
    List<Project> dueDateAfterNowProjects =
        projectNotificationService.getAllByProjectDueDateAfterNow();

    List<GetTargetAmountCheckExceedRequestDto> getTargetAmountCheckExceedRequestDtos =
        dueDateAfterNowProjects.stream()
            .map(
                p ->
                    GetTargetAmountCheckExceedRequestDto.builder()
                        .projectId(p.getProjectId())
                        .projectName(p.getProjectName())
                        .makerMemberId(p.getMaker().getMemberId())
                        .projectTargetAmount(p.getProjectTargetAmount())
                        .build())
            .collect(Collectors.toList());

    // 2. 마감된 프로젝트에 펀딩한 회원들 조회 api call(funding)
    List<GetTargetAmountCheckExceedResponseDto> getTargetAmountCheckExceedResponseDtos =
        fundingServiceClient
            .getTargetAmountCheckExceed(getTargetAmountCheckExceedRequestDtos)
            .getData();
    CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");
    circuitBreaker.run(
        () ->
            fundingServiceClient
                .getTargetAmountCheckExceed(getTargetAmountCheckExceedRequestDtos)
                .getData(),
        throwable -> new FundingServiceClientOutOfServiceException());

    try {
      String jsonString = mapper.writeValueAsString(getTargetAmountCheckExceedResponseDtos);
      // 3. 결과 aws sns에 메시지 게시
      projectNotificationEventPublisher.send(SNS_DESTINATION_NAME, jsonString);

      // 4. sqs 메시지 삭제
      ack.acknowledge();
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
