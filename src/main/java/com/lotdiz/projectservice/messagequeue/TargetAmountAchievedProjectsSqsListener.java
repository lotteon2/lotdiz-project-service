package com.lotdiz.projectservice.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lotdiz.projectservice.client.FundingServiceClient;
import com.lotdiz.projectservice.dto.TargetAmountAchievedProjectsMessageDto;
import com.lotdiz.projectservice.dto.request.ProjectInformationForAchievedTargetAmountRequestDto;
import com.lotdiz.projectservice.dto.response.TargetAmountAchievedProjectsDto;
import com.lotdiz.projectservice.entity.Project;
import com.lotdiz.projectservice.exception.FundingServiceClientOutOfServiceException;
import com.lotdiz.projectservice.mapper.ProjectMapper;
import com.lotdiz.projectservice.repository.ProjectRepository;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class TargetAmountAchievedProjectsSqsListener {

  private final ObjectMapper objectMapper;
  private final FundingServiceClient fundingServiceClient;
  private final ProjectRepository projectRepository;
  private final CircuitBreakerFactory circuitBreakerFactory;
  private final SendTargetAmountAchievedProjects sendTargetAmountAchievedProjects;
  private final ProjectMapper projectMapper;

  @SqsListener(
      value = "${cloud.aws.sqs.funding-target-amount-notification-queue.name}",
      deletionPolicy = SqsMessageDeletionPolicy.NEVER)
  public void achievementOfTargetFundingAmountSqsListener(
      @Payload String message, @Headers Map<String, String> headers, Acknowledgment ack)
      throws JsonProcessingException {
    CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");
    TargetAmountAchievedProjectsMessageDto triggerMessage =
        objectMapper.readValue(message, TargetAmountAchievedProjectsMessageDto.class);
    // event bridge 에서 받아온 트리거 메세지
    if (triggerMessage.getTrigger().equals("fundingTargetAmountCheck")) {
      // 목표 펀딩 금액 달성 수행
      List<Project> allProjects = projectRepository.findAll();
      List<ProjectInformationForAchievedTargetAmountRequestDto> projectInformation =
          projectMapper.getListOfProjectInformationForAchievedTargetAmountRequestDto(allProjects);

      // 목표 펀딩 금액 달성 프로젝트 id, 해당 프로젝트에 참여한 서포터 id, 프로젝트 메이커 id 받아오기
      TargetAmountAchievedProjectsDto targetAmountAchievedProjects =
          (TargetAmountAchievedProjectsDto)
              circuitBreaker.run(
                  () ->
                      fundingServiceClient
                          .getTargetAmountAchievedProjects(projectInformation)
                          .getData(),
                  throwable -> new FundingServiceClientOutOfServiceException());
      ack.acknowledge();
      // sqs 에 목표 펀딩 금액 달성 알림을 위한 데이터 전송
      sendTargetAmountAchievedProjects.sendTargetAmountAchievedProjectsMessageRequest(
          targetAmountAchievedProjects);
    }
  }
}
