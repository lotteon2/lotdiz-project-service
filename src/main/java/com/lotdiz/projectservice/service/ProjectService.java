package com.lotdiz.projectservice.service;

import com.lotdiz.projectservice.client.FundingServiceClient;
import com.lotdiz.projectservice.dto.LotdealDueDateDto;
import com.lotdiz.projectservice.dto.ProjectDto;
import com.lotdiz.projectservice.dto.ProjectThumbnailImageDto;
import com.lotdiz.projectservice.dto.response.FundingAchievementResultMapResponseDto;
import com.lotdiz.projectservice.dto.response.ProjectRegisteredByMakerResponseDto;
import com.lotdiz.projectservice.entity.Maker;
import com.lotdiz.projectservice.entity.Project;
import com.lotdiz.projectservice.entity.ProjectStatus;
import com.lotdiz.projectservice.exception.FundingServiceClientOutOfServiceException;
import com.lotdiz.projectservice.exception.MakerEntityNotfoundException;
import com.lotdiz.projectservice.repository.LotdealRepository;
import com.lotdiz.projectservice.repository.MakerRepository;
import com.lotdiz.projectservice.repository.ProjectImageRepository;
import com.lotdiz.projectservice.repository.ProjectRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectService {

  private final ProjectRepository projectRepository;
  private final FundingServiceClient fundingServiceClient;
  private final CircuitBreakerFactory circuitBreakerFactory;
  private final ProjectImageRepository projectImageRepository;
  private final LotdealRepository lotdealRepository;
  private final MakerRepository makerRepository;

  @Transactional
  public int updateProjectStatusDueDateAfter(ProjectStatus projectStatus, List<Long> projectIds) {
    return projectRepository.updateProjectStatusDueDateAfter(
        projectStatus, LocalDateTime.now(), projectIds);
  }

  public ProjectRegisteredByMakerResponseDto getRegisteredProject(
      Long memberId, Pageable pageable) {
    CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");

    // maker name
    Maker maker =
        makerRepository.findByMemberId(memberId).orElseThrow(MakerEntityNotfoundException::new);

    Page<Project> registeredProjects = projectRepository.findByRegisteredProjects(maker, pageable);
    // 한 페이지에 보여줄 프로젝트 id 목록
    List<Long> projectIds = new ArrayList<>();
    registeredProjects.getContent().forEach(project -> projectIds.add(project.getProjectId()));

    // 프로젝트 id를 key로 하는 펀딩 달성률, 누적 펀딩 금액 데이터
    FundingAchievementResultMapResponseDto projectStatisticDtos =
        (FundingAchievementResultMapResponseDto)
            circuitBreaker.run(
                () -> fundingServiceClient.getRegisteredProject(projectIds).getData(),
                throwable -> new FundingServiceClientOutOfServiceException());

    // 프로젝트 id를 key로 하는 썸네일 이미지
    List<ProjectThumbnailImageDto> projectThumbnailImageDtos =
        projectImageRepository.findProjectThumbnailImageByProjectId(projectIds);
    HashMap<Long, String> thumbnailImage =
        projectThumbnailImageDtos.stream()
            .collect(
                Collectors.toMap(
                    ProjectThumbnailImageDto::getProjectId,
                    ProjectThumbnailImageDto::getThumbnailImage,
                    (existing, replacement) -> replacement,
                    HashMap::new));

    // 프로젝트 id를 key로 하는 lotdeal 마감 날짜
    List<LotdealDueDateDto> lotdealDueDateDtos =
        lotdealRepository.findLotdealDueDateByProjectId(projectIds);
    HashMap<Long, LocalDateTime> lotdealDueDate =
        lotdealDueDateDtos.stream()
            .collect(
                Collectors.toMap(
                    LotdealDueDateDto::getProjectId,
                    LotdealDueDateDto::getLotdealDueDate,
                    (existing, replacement) -> replacement,
                    HashMap::new));

    List<ProjectDto> projectDtoList = new ArrayList<>();
    registeredProjects
        .getContent()
        .forEach(
            project ->
                projectDtoList.add(
                    ProjectDto.builder()
                        .projectId(project.getProjectId())
                        .projectName(project.getProjectName())
                        .projectStatus(String.valueOf(project.getProjectStatus()))
                        .remainingDays(
                            Duration.between(LocalDateTime.now(), project.getProjectDueDate())
                                .toDays())
                        .lotdealDueTime(lotdealDueDate.get(project.getProjectId()))
                        .makerName(maker.getMakerName())
                        .projectThumbnailImageUrl(thumbnailImage.get(project.getProjectId()))
                        .fundingAchievementRate(
                            projectStatisticDtos
                                .getFundingAchievementResultOfProjects()
                                .get(project.getProjectId().toString())
                                .getFundingAchievementRate())
                        .accumulatedFundingAmount(
                            projectStatisticDtos
                                .getFundingAchievementResultOfProjects()
                                .get(project.getProjectId().toString())
                                .getAccumulatedFundingAmount())
                        .build()));

    return ProjectRegisteredByMakerResponseDto.builder().projects(projectDtoList).build();
  }
}
