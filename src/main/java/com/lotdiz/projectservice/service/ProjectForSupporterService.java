package com.lotdiz.projectservice.service;

import com.lotdiz.projectservice.client.FundingServiceClient;
import com.lotdiz.projectservice.client.MemberServiceClient;
import com.lotdiz.projectservice.dto.ProductDto;
import com.lotdiz.projectservice.dto.ProjectImageDto;
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
  public List<ProjectByCategoryResponseDto> getProjectsByCategory(
      String categoryName, Pageable pageable, Long memberId) {

    CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");

    List<ProjectByCategoryResponseDto> projectByCategoryResponseDtoList = new ArrayList<>();
    List<Long> projectIds = new ArrayList<>();

    Page<Project> projects =
        projectRepository.findByCategoryAndProjectIsAuthorized(categoryName, true, pageable);

    projects.forEach(p -> projectIds.add(p.getProjectId()));

    Map<String, Boolean> likedProjects = getIsLikeClient(circuitBreaker, memberId, projectIds);

    HashMap<String, FundingAchievementResultOfProjectResponseDto>
        fundingAchievementResultOfProjectResponseDtoList =
            getFundingProjectClient(circuitBreaker, projectIds);

    for (Project p : projects) {
      Lotdeal lotdeal = lotdealRepository.findByProjectAndLotdealing(p, LocalDateTime.now());

      ProjectByCategoryResponseDto projectByCategoryResponseDto =
          ProjectByCategoryResponseDto.toDto(
              p,
              likedProjects.get(Long.toString(p.getProjectId())),
              fundingAchievementResultOfProjectResponseDtoList.get(Long.toString(p.getProjectId())),
              lotdeal);

      projectByCategoryResponseDtoList.add(projectByCategoryResponseDto);
    }
    return projectByCategoryResponseDtoList;
  }

  @Transactional(readOnly = true)
  public ProjectDetailResponseDto getProjectDetails(Long projectId) {

    CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");

    Project project =
        projectRepository.findById(projectId).orElseThrow(ProjectEntityNotFoundException::new);

    List<ProjectImageDto> projectImageDtoList =
        projectImageMapper.projectImageEntityToProjectImageDtoList(
            projectImageRepository.findByProject(project));

    List<ProductDto> productDtoList =
        productMapper.productEntityToProductDtoList(productRepository.findByProject(project));

    Long numberOfSupporter = supportSignatureRepository.countByProject(project);

    Long likeCount = getLikeCountClient(circuitBreaker, projectId);

    FundingAchievementResultOfProjectDetailResponseDto
        fundingAchievementResultOfProjectDetailResponseDto =
            getFundingOfProjectDetailClient(circuitBreaker, projectId);

    Lotdeal lotdeal = lotdealRepository.findByProjectAndLotdealing(project, LocalDateTime.now());

    ProjectDetailResponseDto projectDetailResponseDto =
        ProjectDetailResponseDto.toDto(
            project,
            projectImageDtoList,
            productDtoList,
            likeCount,
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
      throw new DuplicateKeyException("지지서명에 중복된 데이터가 있습니다.");
    }
    supportSignatureRepository.save(supportSignature);
  }

  @Transactional(readOnly = true)
  public List<SupportSignatureResponseDto> getSupportSignature(Long projectId, Pageable pageable) {

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
              s, memberInfoResponseDtos.get(Long.toString(s.getMemberId()))));
    }

    return supportSignatureResponseDtoList;
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
  public List<LotdealProjectResponseDto> getLotdeal(Pageable pageable, Long memberId) {

    CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");

    List<LotdealProjectResponseDto> lotdealProjectResponseDtoList = new ArrayList<>();

    List<Long> projectIds = new ArrayList<>();
    Page<Lotdeal> lotdealProjects =
        lotdealRepository.findAllLotdealing(LocalDateTime.now(), true, pageable);

    lotdealProjects.forEach(l -> projectIds.add(l.getProject().getProjectId()));

    Map<String, Boolean> likedProjects = getIsLikeClient(circuitBreaker, memberId, projectIds);

    HashMap<String, FundingAchievementResultOfProjectResponseDto>
        fundingAchievementResultOfProjectResponseDtos =
            getFundingProjectClient(circuitBreaker, projectIds);

    for (Lotdeal lotdeal : lotdealProjects) {
      LotdealProjectResponseDto lotdealProjectResponseDto =
          LotdealProjectResponseDto.toDto(
              lotdeal.getProject(),
              likedProjects.get(Long.toString(lotdeal.getProject().getProjectId())),
              fundingAchievementResultOfProjectResponseDtos.get(
                  Long.toString(lotdeal.getProject().getProjectId())),
              lotdeal.getLotdealDueTime());

      lotdealProjectResponseDtoList.add(lotdealProjectResponseDto);
    }

    return lotdealProjectResponseDtoList;
  }

  @Transactional(readOnly = true)
  public List<BannerResponseDto> getBanners() {
    return bannerMapper.toBannerDtoList(bannerRepository.findAll());
  }

  // feign client
  public HashMap<String, FundingAchievementResultOfProjectResponseDto> getFundingProjectClient(
      CircuitBreaker circuitBreaker, List<Long> projectIds) {

    return (HashMap<String, FundingAchievementResultOfProjectResponseDto>)
        circuitBreaker.run(
            () -> fundingServiceClient.getFundingOfProject(projectIds).getData(),
            throwable -> new FundingServiceClientOutOfServiceException());
  }

  @Transactional(readOnly = true)
  public List<SpecialExhibitionResponseDto> getSpecialExhibition(
      Pageable pageable, String tag, Long memberId) {

    CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");

    List<SpecialExhibitionResponseDto> specialExhibitionResponseDtoList = new ArrayList<>();
    List<Long> projectIds = new ArrayList<>();

    Page<Project> projects =
        projectRepository.findByProjectTagAndProjectIsAuthorized(tag, true, pageable);

    projects.forEach(p -> projectIds.add(p.getProjectId()));

    Map<String, Boolean> likedProjects = getIsLikeClient(circuitBreaker, memberId, projectIds);

    HashMap<String, FundingAchievementResultOfProjectResponseDto>
        fundingAchievementResultOfProjectResponseDtoList =
            getFundingProjectClient(circuitBreaker, projectIds);

    for (Project p : projects) {
      Lotdeal lotdeal = lotdealRepository.findByProjectAndLotdealing(p, LocalDateTime.now());

      SpecialExhibitionResponseDto specialExhibitionResponseDto =
          SpecialExhibitionResponseDto.toDto(
              p,
              likedProjects.get(Long.toString(p.getProjectId())),
              fundingAchievementResultOfProjectResponseDtoList.get(Long.toString(p.getProjectId())),
              lotdeal);
      specialExhibitionResponseDtoList.add(specialExhibitionResponseDto);
    }

    return specialExhibitionResponseDtoList;
  }

  public FundingAchievementResultOfProjectDetailResponseDto getFundingOfProjectDetailClient(
      CircuitBreaker circuitBreaker, Long projectId) {

    return (FundingAchievementResultOfProjectDetailResponseDto)
        circuitBreaker.run(
            () -> fundingServiceClient.getFundingOfProjectDetail(projectId).getData(),
            throwable -> new FundingServiceClientOutOfServiceException());
  }

  public Map<String, Boolean> getIsLikeClient(
      CircuitBreaker circuitBreaker, Long memberId, List<Long> projectIds) {

    return (Map<String, Boolean>)
        circuitBreaker.run(
            () -> memberServiceClient.getIsLike(memberId, projectIds).getData(),
            throwable -> new MemberServiceClientOutOfServiceException());
  }

  public Long getLikeCountClient(CircuitBreaker circuitBreaker, Long projectId) {
    return (Long)
        circuitBreaker.run(
            () ->
                memberServiceClient.getLikeCount(projectId).getData().get(Long.toString(projectId)),
            throwable -> new MemberServiceClientOutOfServiceException());
  }

  public Map<String, MemberInfoResponseDto> getMemberInfoClient(
      CircuitBreaker circuitBreaker, List<Long> memberIds) {

    return (Map<String, MemberInfoResponseDto>)
        circuitBreaker.run(
            () -> memberServiceClient.getMemberInfo(memberIds).getData(),
            throwable -> new MemberServiceClientOutOfServiceException());
  }
}
