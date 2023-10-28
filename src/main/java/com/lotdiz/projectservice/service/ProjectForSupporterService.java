package com.lotdiz.projectservice.service;

import com.lotdiz.projectservice.client.FundingServiceClient;
import com.lotdiz.projectservice.client.MemberServiceClient;
import com.lotdiz.projectservice.dto.ProductDto;
import com.lotdiz.projectservice.dto.ProjectImageDto;
import com.lotdiz.projectservice.dto.request.FundingAchievementResultOfProjectRequestDto;
import com.lotdiz.projectservice.dto.request.SupportSignatureRequestDto;
import com.lotdiz.projectservice.dto.response.*;
import com.lotdiz.projectservice.entity.Lotdeal;
import com.lotdiz.projectservice.entity.Project;
import com.lotdiz.projectservice.entity.SupportSignature;
import com.lotdiz.projectservice.exception.FundingServiceClientOutOfServiceException;
import com.lotdiz.projectservice.exception.MemberServiceClientOutOfServiceException;
import com.lotdiz.projectservice.exception.ProjectEntityNotFoundException;
import com.lotdiz.projectservice.exception.SupportSignatureEntityNotFoundException;
import com.lotdiz.projectservice.mapper.BannerMapper;
import com.lotdiz.projectservice.mapper.ProductMapper;
import com.lotdiz.projectservice.mapper.ProjectImageMapper;
import com.lotdiz.projectservice.repository.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectForSupporterService {

  private final ProjectRepository projectRepository;
  private final LotdealRepository lotdealRepository;
  private final ProjectImageRepository projectImageRepository;
  private final ProductRepository productRepository;
  private final SupportSignatureRepository supportSignatureRepository;
  private final BannerRepository bannerRepository;
  private final FundingServiceClient fundingServiceClient;
  private final MemberServiceClient memberServiceClient;
  private final BannerMapper bannerMapper;
  private final ProductMapper productMapper;
  private final ProjectImageMapper projectImageMapper;
  private final CircuitBreakerFactory circuitBreakerFactory;

  @Transactional(readOnly = true)
  public PagedDataResponseDto<Object> getLotdPlusProject(Long memberId) {

    CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");

    List<BestLotdPlusResponseDto> bestLotdPlusResponseDtos = new ArrayList<>();
    List<Long> projectIds = new ArrayList<>();

    Page<Project> bestlotdPlusList =
        projectRepository.findBestLotdPlus(LocalDateTime.now(), PageRequest.of(0, 6));

    List<FundingAchievementResultOfProjectRequestDto> fundingAchievementResultOfProjectRequestDtos =
        new ArrayList<>();

    bestlotdPlusList.forEach(
        p -> {
          projectIds.add(p.getProjectId());
          fundingAchievementResultOfProjectRequestDtos.add(
              FundingAchievementResultOfProjectRequestDto.builder()
                  .projectId(p.getProjectId())
                  .projectTargetAmount(p.getProjectTargetAmount())
                  .build());
        });

    Map<String, Boolean> likedProjects = getIsLikeClient(circuitBreaker, memberId, projectIds);

    HashMap<String, FundingAchievementResultOfProjectResponseDto>
        fundingAchievementResultOfProjectResponseDtoList =
            getFundingProjectClient(circuitBreaker, fundingAchievementResultOfProjectRequestDtos);

    for (Project lotPlus : bestlotdPlusList) {
      BestLotdPlusResponseDto bestLotdPlusResponseDto =
          BestLotdPlusResponseDto.toDto(
              lotPlus,
              projectImageRepository.findProjectImageByProjectAndAndProjectImageIsThumbnail(
                  lotPlus, true),
              likedProjects.get(Long.toString(lotPlus.getProjectId())),
              fundingAchievementResultOfProjectResponseDtoList.get(
                  Long.toString(lotPlus.getProjectId())));

      bestLotdPlusResponseDtos.add(bestLotdPlusResponseDto);
    }

    return PagedDataResponseDto.builder()
        .totalPages(bestlotdPlusList.getTotalPages())
        .dataList(bestLotdPlusResponseDtos)
        .build();
  }

  @Transactional(readOnly = true)
  public PagedDataResponseDto<Object> getProjectsByCategory(
      String categoryName, Pageable pageable, Long memberId) {

    CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");

    List<ProjectByCategoryResponseDto> projectByCategoryResponseDtoList = new ArrayList<>();
    List<Long> projectIds = new ArrayList<>();
    List<FundingAchievementResultOfProjectRequestDto> fundingAchievementResultOfProjectRequestDtos =
        new ArrayList<>();

    Page<Project> projects =
        projectRepository.findByCategoryAndProjectIsAuthorized(categoryName, true, pageable);

    projects.forEach(
        p -> {
          projectIds.add(p.getProjectId());
          fundingAchievementResultOfProjectRequestDtos.add(
              FundingAchievementResultOfProjectRequestDto.builder()
                  .projectId(p.getProjectId())
                  .projectTargetAmount(p.getProjectTargetAmount())
                  .build());
        });

    Map<String, Boolean> likedProjects = getIsLikeClient(circuitBreaker, memberId, projectIds);

    HashMap<String, FundingAchievementResultOfProjectResponseDto>
        fundingAchievementResultOfProjectResponseDtoList =
            getFundingProjectClient(circuitBreaker, fundingAchievementResultOfProjectRequestDtos);

    for (Project p : projects) {
      Lotdeal lotdeal = lotdealRepository.findByProjectAndLotdealing(p, LocalDateTime.now());

      ProjectByCategoryResponseDto projectByCategoryResponseDto =
          ProjectByCategoryResponseDto.toDto(
              p,
              projectImageRepository.findProjectImageByProjectAndAndProjectImageIsThumbnail(
                  p, true),
              likedProjects.get(Long.toString(p.getProjectId())),
              fundingAchievementResultOfProjectResponseDtoList.get(Long.toString(p.getProjectId())),
              lotdeal);

      projectByCategoryResponseDtoList.add(projectByCategoryResponseDto);
    }
    return PagedDataResponseDto.builder()
        .totalPages(projects.getTotalPages())
        .dataList(projectByCategoryResponseDtoList)
        .build();
  }

  @Transactional(readOnly = true)
  public ProjectDetailResponseDto getProjectDetails(Long projectId, Long memberId) {

    CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");

    Project project =
        projectRepository.findById(projectId).orElseThrow(ProjectEntityNotFoundException::new);

    List<ProjectImageDto> projectImageDtoList =
        projectImageMapper.projectImageEntityToProjectImageDtoList(
            projectImageRepository.findByProject(project));

    List<ProductDto> productDtoList =
        productMapper.productEntityToProductDtoList(productRepository.findByProject(project));

    Long numberOfSupporter = supportSignatureRepository.countByProject(project);

    MemberLikesInfoResponseDto likesInfo = getLikeInfoClient(circuitBreaker, projectId, memberId);

    FundingAchievementResultOfProjectDetailResponseDto
        fundingAchievementResultOfProjectDetailResponseDto =
            getFundingOfProjectDetailClient(
                circuitBreaker, projectId, project.getProjectTargetAmount());

    Lotdeal lotdeal = lotdealRepository.findByProjectAndLotdealing(project, LocalDateTime.now());

    ProjectDetailResponseDto projectDetailResponseDto =
        ProjectDetailResponseDto.toDto(
            project,
            projectImageDtoList,
            productDtoList,
            likesInfo,
            fundingAchievementResultOfProjectDetailResponseDto,
            numberOfSupporter,
            lotdeal);

    return projectDetailResponseDto;
  }

  @Transactional
  public void createSupportSignature(
      Long memberId, Long projectId, SupportSignatureRequestDto supportSignatureContents) {

    Project project =
        projectRepository.findById(projectId).orElseThrow(ProjectEntityNotFoundException::new);

    SupportSignature supportSignature =
        SupportSignature.builder()
            .project(project)
            .memberId(memberId)
            .supportSignatureContent(supportSignatureContents.getSupportSignatureContents())
            .build();

    if (supportSignatureRepository.existsByProjectAndMemberId(project, memberId)) {
      throw new DuplicateKeyException("이미 작성된 지지서명이 존재합니다.");
    }
    supportSignatureRepository.save(supportSignature);
  }

  @Transactional(readOnly = true)
  public PagedDataResponseDto<Object> getSupportSignature(Long projectId, Long memberId, Pageable pageable) {

    CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");
    List<SupportSignatureResponseDto> supportSignatureResponseDtoList = new ArrayList<>();

    Project project =
        projectRepository.findById(projectId).orElseThrow(ProjectEntityNotFoundException::new);

    Page<SupportSignature> supportSignatureList =
        supportSignatureRepository.findByProject(project, pageable);

    List<Long> memberIds =
        supportSignatureList.stream()
            .map(supportSignature -> supportSignature.getMemberId())
            .collect(Collectors.toList());

    Map<String, MemberInfoResponseDto> memberInfoResponseDtos =
        getMemberInfoClient(circuitBreaker, memberIds);

    for (SupportSignature s : supportSignatureList) {
      supportSignatureResponseDtoList.add(
          SupportSignatureResponseDto.toDto(
              s, memberInfoResponseDtos.get(Long.toString(s.getMemberId())), memberId));
    }

    return PagedDataResponseDto.builder()
        .totalPages(supportSignatureList.getTotalPages())
        .dataList(supportSignatureResponseDtoList)
        .build();
  }

  @Transactional
  public void modifySupportSignature(
      Long memberId, Long projectId, SupportSignatureRequestDto supportSignatureContentDto) {

    Project project =
        projectRepository.findById(projectId).orElseThrow(ProjectEntityNotFoundException::new);

    SupportSignature supportSignature =
        supportSignatureRepository
            .findByProjectAndMemberId(project, memberId)
            .orElseThrow(SupportSignatureEntityNotFoundException::new);
    supportSignature.setSupportSignatureContent(
        supportSignatureContentDto.getSupportSignatureContents());

    supportSignatureRepository.save(supportSignature);
  }

  @Transactional(readOnly = true)
  public PagedDataResponseDto<Object> getLotdeal(Pageable pageable, Long memberId) {

    CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");

    List<LotdealProjectResponseDto> lotdealProjectResponseDtoList = new ArrayList<>();

    List<Long> projectIds = new ArrayList<>();
    List<FundingAchievementResultOfProjectRequestDto> fundingAchievementResultOfProjectRequestDtos =
        new ArrayList<>();
    Page<Lotdeal> lotdealProjects =
        lotdealRepository.findAllLotdealing(LocalDateTime.now(), true, pageable);

    lotdealProjects.forEach(
        p -> {
          projectIds.add(p.getProject().getProjectId());
          fundingAchievementResultOfProjectRequestDtos.add(
              FundingAchievementResultOfProjectRequestDto.builder()
                  .projectId(p.getProject().getProjectId())
                  .projectTargetAmount(p.getProject().getProjectTargetAmount())
                  .build());
        });

    Map<String, Boolean> likedProjects = getIsLikeClient(circuitBreaker, memberId, projectIds);

    HashMap<String, FundingAchievementResultOfProjectResponseDto>
        fundingAchievementResultOfProjectResponseDtos =
            getFundingProjectClient(circuitBreaker, fundingAchievementResultOfProjectRequestDtos);

    for (Lotdeal lotdeal : lotdealProjects) {
      LotdealProjectResponseDto lotdealProjectResponseDto =
          LotdealProjectResponseDto.toDto(
              lotdeal.getProject(),
              projectImageRepository.findProjectImageByProjectAndAndProjectImageIsThumbnail(
                  lotdeal.getProject(), true),
              likedProjects.get(Long.toString(lotdeal.getProject().getProjectId())),
              fundingAchievementResultOfProjectResponseDtos.get(
                  Long.toString(lotdeal.getProject().getProjectId())),
              lotdeal.getLotdealDueTime());

      lotdealProjectResponseDtoList.add(lotdealProjectResponseDto);
    }

    return PagedDataResponseDto.builder()
        .totalPages(lotdealProjects.getTotalPages())
        .dataList(lotdealProjectResponseDtoList)
        .build();
  }

  @Transactional(readOnly = true)
  public List<BannerResponseDto> getBanners() {
    return bannerMapper.toBannerDtoList(bannerRepository.findAll());
  }

  @Transactional(readOnly = true)
  public PagedDataResponseDto<Object> getSpecialExhibition(
      Pageable pageable, String tag, Long memberId) {

    CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");

    List<SpecialExhibitionResponseDto> specialExhibitionResponseDtoList = new ArrayList<>();
    List<Long> projectIds = new ArrayList<>();
    List<FundingAchievementResultOfProjectRequestDto> fundingAchievementResultOfProjectRequestDtos =
        new ArrayList<>();

    Page<Project> projects =
        projectRepository.findByProjectTagAndProjectIsAuthorized(tag, true, pageable);

    projects.forEach(
        p -> {
          projectIds.add(p.getProjectId());
          fundingAchievementResultOfProjectRequestDtos.add(
              FundingAchievementResultOfProjectRequestDto.builder()
                  .projectId(p.getProjectId())
                  .projectTargetAmount(p.getProjectTargetAmount())
                  .build());
        });

    Map<String, Boolean> likedProjects = getIsLikeClient(circuitBreaker, memberId, projectIds);

    HashMap<String, FundingAchievementResultOfProjectResponseDto>
        fundingAchievementResultOfProjectResponseDtoList =
            getFundingProjectClient(circuitBreaker, fundingAchievementResultOfProjectRequestDtos);

    for (Project p : projects) {
      Lotdeal lotdeal = lotdealRepository.findByProjectAndLotdealing(p, LocalDateTime.now());

      SpecialExhibitionResponseDto specialExhibitionResponseDto =
          SpecialExhibitionResponseDto.toDto(
              p,
              projectImageRepository.findProjectImageByProjectAndAndProjectImageIsThumbnail(
                  p, true),
              likedProjects.get(Long.toString(p.getProjectId())),
              fundingAchievementResultOfProjectResponseDtoList.get(Long.toString(p.getProjectId())),
              lotdeal);
      specialExhibitionResponseDtoList.add(specialExhibitionResponseDto);
    }

    return PagedDataResponseDto.builder()
        .totalPages(projects.getTotalPages())
        .dataList(specialExhibitionResponseDtoList)
        .build();
  }

  @Transactional(readOnly = true)
  public Boolean getIsSupportSignature(Long memberId, Long projectId) {
    Project project =
        projectRepository.findById(projectId).orElseThrow(ProjectEntityNotFoundException::new);

    return supportSignatureRepository.existsByProjectAndMemberId(project, memberId);
  }

  // feign client
  public HashMap<String, FundingAchievementResultOfProjectResponseDto> getFundingProjectClient(
      CircuitBreaker circuitBreaker,
      List<FundingAchievementResultOfProjectRequestDto>
          fundingAchievementResultOfProjectRequestDtos) {

    return (HashMap<String, FundingAchievementResultOfProjectResponseDto>)
        circuitBreaker.run(
            () ->
                fundingServiceClient
                    .getFundingOfProject(fundingAchievementResultOfProjectRequestDtos)
                    .getData(),
            throwable -> new FundingServiceClientOutOfServiceException());
  }

  public FundingAchievementResultOfProjectDetailResponseDto getFundingOfProjectDetailClient(
      CircuitBreaker circuitBreaker, Long projectId, Long projectTargetAmount) {

    return (FundingAchievementResultOfProjectDetailResponseDto)
        circuitBreaker.run(
            () ->
                fundingServiceClient
                    .getFundingOfProjectDetail(projectId, projectTargetAmount)
                    .getData(),
            throwable -> new FundingServiceClientOutOfServiceException());
  }

  public Map<String, Boolean> getIsLikeClient(
      CircuitBreaker circuitBreaker, Long memberId, List<Long> projectIds) {

    Map<String, Boolean> isLikeMap = new HashMap<>();

    if (memberId != null) {
      isLikeMap =
          (Map<String, Boolean>)
              circuitBreaker.run(
                  () -> memberServiceClient.getIsLike(memberId, projectIds).getData(),
                  throwable -> new MemberServiceClientOutOfServiceException());
    }
    return isLikeMap;
  }

  public MemberLikesInfoResponseDto getLikeInfoClient(
      CircuitBreaker circuitBreaker, Long projectId, Long memberId) {
    MemberLikesInfoResponseDto memberLikesInfoResponseDto = new MemberLikesInfoResponseDto();

    if (memberId != null) {
      memberLikesInfoResponseDto =
          (MemberLikesInfoResponseDto)
              circuitBreaker.run(
                  () -> memberServiceClient.getLikesWithMemberId(projectId, memberId).getData(),
                  throwable -> new MemberServiceClientOutOfServiceException());

    } else {
      memberLikesInfoResponseDto =
          (MemberLikesInfoResponseDto)
              circuitBreaker.run(
                  () -> memberServiceClient.getLikes(projectId).getData(),
                  throwable -> new MemberServiceClientOutOfServiceException());
    }
    return memberLikesInfoResponseDto;
  }

  public Map<String, MemberInfoResponseDto> getMemberInfoClient(
      CircuitBreaker circuitBreaker, List<Long> memberIds) {

    return (Map<String, MemberInfoResponseDto>)
        circuitBreaker.run(
            () -> memberServiceClient.getMemberInfo(memberIds).getData(),
            throwable -> new MemberServiceClientOutOfServiceException());
  }
}
