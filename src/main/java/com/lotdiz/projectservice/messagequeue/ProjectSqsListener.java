package com.lotdiz.projectservice.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lotdiz.projectservice.client.FundingServiceClient;
import com.lotdiz.projectservice.dto.request.GetTargetAmountCheckExceedRequestDto;
import com.lotdiz.projectservice.dto.response.GetTargetAmountCheckExceedResponseDto;
import com.lotdiz.projectservice.entity.Project;
import com.lotdiz.projectservice.entity.ProjectStatus;
import com.lotdiz.projectservice.exception.FundingServiceClientOutOfServiceException;
import com.lotdiz.projectservice.exception.ProjectCannotUpdatableException;
import com.lotdiz.projectservice.mapper.ProjectMapper;
import com.lotdiz.projectservice.service.ProjectNotificationService;
import com.lotdiz.projectservice.service.ProjectService;
import com.lotdiz.projectservice.sns.ProjectNotificationEventPublisher;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.listener.Acknowledgment;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
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
  private final ProjectMapper projectMapper;
  private final FundingServiceClient fundingServiceClient;

  private final ProjectService projectService;
  private final ProjectNotificationService projectNotificationService;
  private final ProjectNotificationEventPublisher projectNotificationEventPublisher;

  private void fundingServiceClientOutOfServiceException(Throwable t) {
    log.error(t.getMessage());
    throw new FundingServiceClientOutOfServiceException();
  }

  @CircuitBreaker(
      name = "circuitBreaker",
      fallbackMethod = "fundingServiceClientOutOfServiceException")
  @SqsListener(
      value = "${cloud.aws.sqs.project-due-date-event-queue.name}",
      deletionPolicy = SqsMessageDeletionPolicy.NEVER)
  public void createProjectDueDateNotificationQueue(
      @Payload String message, @Headers Map<String, String> headers, Acknowledgment ack) {
    log.info("receive ProjectDueDateEvent message={}", message);

    // 마감된 프로젝트 조회
    List<Project> dueDateAfterNowProjects =
        projectNotificationService.getAllByProjectDueDateAfterNow();
    // 마감된 프로젝트가 있다면
    if (!dueDateAfterNowProjects.isEmpty()) {
      List<GetTargetAmountCheckExceedRequestDto> getTargetAmountCheckExceedRequestDtos =
          projectMapper.projectsToGetTargetAmountCheckExceedRequestDtos(dueDateAfterNowProjects);

      // funding service 요청
      List<GetTargetAmountCheckExceedResponseDto> getTargetAmountCheckExceedResponseDtos =
          fundingServiceClient
              .getTargetAmountCheckExceed(getTargetAmountCheckExceedRequestDtos)
              .getData();

      try {
        // 성공 펀딩 상태 업데이트
        List<Long> successProjectIds = getSuccessProjectIds(getTargetAmountCheckExceedResponseDtos);
        if ((projectService.updateProjectStatusDueDateAfter(
                ProjectStatus.SUCCESS, successProjectIds))
            != successProjectIds.size()) {
          throw new ProjectCannotUpdatableException();
        }

        // 미달성 펀딩 상태 업데이트
        List<Long> failedProjectIds = getFailedProjectIds(getTargetAmountCheckExceedResponseDtos);
        if ((projectService.updateProjectStatusDueDateAfter(ProjectStatus.FAIL, failedProjectIds))
            != failedProjectIds.size()) {
          throw new ProjectCannotUpdatableException();
        }

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

  @NotNull
  private static List<Long> getFailedProjectIds(
      List<GetTargetAmountCheckExceedResponseDto> getTargetAmountCheckExceedResponseDtos) {
    return getTargetAmountCheckExceedResponseDtos.stream()
        .filter(p -> !p.getIsTargetAmountExceed())
        .map(GetTargetAmountCheckExceedResponseDto::getProjectId)
        .collect(Collectors.toList());
  }

  @NotNull
  private static List<Long> getSuccessProjectIds(
      List<GetTargetAmountCheckExceedResponseDto> getTargetAmountCheckExceedResponseDtos) {
    return getTargetAmountCheckExceedResponseDtos.stream()
        .filter(GetTargetAmountCheckExceedResponseDto::getIsTargetAmountExceed)
        .map(GetTargetAmountCheckExceedResponseDto::getProjectId)
        .collect(Collectors.toList());
  }
}
