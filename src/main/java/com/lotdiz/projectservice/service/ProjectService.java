package com.lotdiz.projectservice.service;

import com.lotdiz.projectservice.client.DeliveryServiceClient;
import com.lotdiz.projectservice.client.FundingServiceClient;
import com.lotdiz.projectservice.client.MemberServiceClient;
import com.lotdiz.projectservice.dto.DeliveryStatusOfFundingDto;
import com.lotdiz.projectservice.dto.MemberFundingInformationDto;
import com.lotdiz.projectservice.dto.MemberNameDto;
import com.lotdiz.projectservice.dto.RegisteredProjectFundingDto;
import com.lotdiz.projectservice.dto.response.DeliveryStatusResponseDto;
import com.lotdiz.projectservice.dto.response.MemberInformationOfFundingResponseDto;
import com.lotdiz.projectservice.dto.response.MemberNameResponseDto;
import com.lotdiz.projectservice.dto.response.RegisteredProjectFundingListResponseDto;
import com.lotdiz.projectservice.entity.ProjectStatus;
import com.lotdiz.projectservice.exception.DeliveryServiceClientOutOfServiceException;
import com.lotdiz.projectservice.exception.FundingServiceClientOutOfServiceException;
import com.lotdiz.projectservice.exception.MemberServiceClientOutOfServiceException;
import com.lotdiz.projectservice.repository.ProjectRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectService {

  private final ProjectRepository projectRepository;
  private final FundingServiceClient fundingServiceClient;
  private final MemberServiceClient memberServiceClient;
  private final DeliveryServiceClient deliveryServiceClient;
  private final CircuitBreakerFactory circuitBreakerFactory;

  @Transactional
  public int updateProjectStatusDueDateAfter(ProjectStatus projectStatus, List<Long> projectIds) {
    return projectRepository.updateProjectStatusDueDateAfter(
        projectStatus, LocalDateTime.now(), projectIds);
  }

  public RegisteredProjectFundingListResponseDto getFundingListOfRegisteredProject(Long projectId) {
    CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");

    // funding list - member id, total amount, funding date 가져오기
    MemberInformationOfFundingResponseDto memberFundingInformationResponseDto =
        (MemberInformationOfFundingResponseDto)
            circuitBreaker.run(
                () -> fundingServiceClient.getFundingList(projectId).getData(),
                throwable -> new FundingServiceClientOutOfServiceException());

    List<Long> memberIds =
        memberFundingInformationResponseDto.getMemberFundingInformationDtos().stream()
            .map(MemberFundingInformationDto::getMemberId)
            .collect(Collectors.toList());

    List<Long> fundingIds =
        memberFundingInformationResponseDto.getMemberFundingInformationDtos().stream()
            .map(MemberFundingInformationDto::getFundingId)
            .collect(Collectors.toList());

    // member id list를 가지고 사용자 이름 가져오기
    MemberNameResponseDto memberNameResponseDto =
        (MemberNameResponseDto)
            circuitBreaker.run(
                () -> memberServiceClient.getMemberName(memberIds).getData(),
                throwable -> new MemberServiceClientOutOfServiceException());

    // delivery 에서 배송 정보
    DeliveryStatusResponseDto deliveryStatusResponseDto =
        (DeliveryStatusResponseDto)
            circuitBreaker.run(
                () -> deliveryServiceClient.getDeliveryStatus(fundingIds).getData(),
                throwable -> new DeliveryServiceClientOutOfServiceException());

    Map<Long, MemberNameDto> memberNameDtos =
        memberNameResponseDto.getMemberNameDtos().stream()
            .collect(Collectors.toMap(MemberNameDto::getMemberId, Function.identity()));

    Map<Long, DeliveryStatusOfFundingDto> deliveryStatusOfFundingDtos =
        deliveryStatusResponseDto.getDeliveryStatusOfFundingDtos().stream()
            .collect(
                Collectors.toMap(DeliveryStatusOfFundingDto::getFundingId, Function.identity()));
    List<RegisteredProjectFundingDto> registeredProjectFundingDtos =
        memberFundingInformationResponseDto.getMemberFundingInformationDtos().stream()
            .map(
                item ->
                    RegisteredProjectFundingDto.builder()
                        .supporterName(memberNameDtos.get(item.getMemberId()).getMemberName())
                        .fundingDate(item.getFundingDate())
                        .totalFundingAmount(item.getFundingTotalAmount())
                        .deliveryStatus(
                            deliveryStatusOfFundingDtos
                                .get(item.getFundingId())
                                .getDeliveryStatus())
                        .build())
            .collect(Collectors.toList());
    return RegisteredProjectFundingListResponseDto.builder()
        .registeredProjectFundingDtos(registeredProjectFundingDtos)
        .build();
  }
}
