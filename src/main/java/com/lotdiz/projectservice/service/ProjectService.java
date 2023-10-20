package com.lotdiz.projectservice.service;

import com.lotdiz.projectservice.client.DeliveryServiceClient;
import com.lotdiz.projectservice.client.FundingServiceClient;
import com.lotdiz.projectservice.client.MemberServiceClient;
import com.lotdiz.projectservice.dto.DeliveryStatusOfFundingDto;
import com.lotdiz.projectservice.dto.MemberFundingInformationDto;
import com.lotdiz.projectservice.dto.MemberNameDto;
import com.lotdiz.projectservice.dto.RegisteredProjectFundingDto;
import com.lotdiz.projectservice.client.FundingServiceClient;
import com.lotdiz.projectservice.dto.LotdealDueDateDto;
import com.lotdiz.projectservice.dto.ProductInformationForRegisteredProjectDto;
import com.lotdiz.projectservice.dto.ProjectAmountWithIdDto;
import com.lotdiz.projectservice.dto.ProjectDto;
import com.lotdiz.projectservice.dto.ProjectThumbnailImageDto;
import com.lotdiz.projectservice.dto.request.MakerRegisterRequestDto;
import com.lotdiz.projectservice.dto.request.ProductInfoForProjectRequestDto;
import com.lotdiz.projectservice.dto.request.ProjectAmountWithIdRequestDto;
import com.lotdiz.projectservice.dto.request.ProjectRegisterInformationRequestDto;
import com.lotdiz.projectservice.dto.response.DeliveryStatusResponseDto;
import com.lotdiz.projectservice.dto.response.MemberInformationOfFundingResponseDto;
import com.lotdiz.projectservice.dto.response.MemberNameResponseDto;
import com.lotdiz.projectservice.dto.response.RegisteredProjectFundingListResponseDto;
import com.lotdiz.projectservice.dto.response.FundingAchievementResultMapResponseDto;
import com.lotdiz.projectservice.dto.response.FundingAchievementResultOfProjectDetailResponseDto;
import com.lotdiz.projectservice.dto.response.ProjectRegisteredByMakerResponseDto;
import com.lotdiz.projectservice.dto.response.RegisteredProjectDetailForStatusResponseDto;
import com.lotdiz.projectservice.entity.Category;
import com.lotdiz.projectservice.entity.Lotdeal;
import com.lotdiz.projectservice.entity.Maker;
import com.lotdiz.projectservice.entity.Product;
import com.lotdiz.projectservice.entity.Project;
import com.lotdiz.projectservice.entity.ProjectImage;
import com.lotdiz.projectservice.entity.ProjectStatus;
import com.lotdiz.projectservice.exception.CategoryNotfoundException;
import com.lotdiz.projectservice.exception.DeliveryServiceClientOutOfServiceException;
import com.lotdiz.projectservice.exception.FundingServiceClientOutOfServiceException;
import com.lotdiz.projectservice.exception.MemberServiceClientOutOfServiceException;
import com.lotdiz.projectservice.exception.FundingServiceClientOutOfServiceException;
import com.lotdiz.projectservice.exception.MakerEntityNotfoundException;
import com.lotdiz.projectservice.exception.ProjectEntityNotFoundException;
import com.lotdiz.projectservice.mapper.MakerMapper;
import com.lotdiz.projectservice.mapper.ProductMapper;
import com.lotdiz.projectservice.mapper.ProjectMapper;
import com.lotdiz.projectservice.messagequeue.kafka.MakerProducer;
import com.lotdiz.projectservice.messagequeue.kafka.ProjectProducer;
import com.lotdiz.projectservice.repository.CategoryRepository;
import com.lotdiz.projectservice.repository.LotdealRepository;
import com.lotdiz.projectservice.repository.MakerRepository;
import com.lotdiz.projectservice.repository.ProductRepository;
import com.lotdiz.projectservice.repository.ProjectImageRepository;
import com.lotdiz.projectservice.repository.ProjectRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
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
  private final MemberServiceClient memberServiceClient;
  private final DeliveryServiceClient deliveryServiceClient;
  private final CircuitBreakerFactory circuitBreakerFactory;
  private final CategoryRepository categoryRepository;
  private final MakerRepository makerRepository;
  private final ProductRepository productRepository;
  private final ProductMapper productMapper;
  private final ProjectImageRepository projectImageRepository;
  private final LotdealRepository lotdealRepository;
  private final MakerMapper makerMapper;
  private final MakerProducer makerProducer;
  private final ProjectProducer projectProducer;
  private final ProjectMapper projectMapper;

  private static ProjectImage getProjectImage(Project project, String url, boolean isThumbnail) {
    return ProjectImage.builder()
        .project(project)
        .projectImageUrl(url)
        .projectImageIsThumbnail(isThumbnail)
        .build();
  }

  private static Product getProduct(
      Project project, ProductInfoForProjectRequestDto productInfoForProjectRequestDto) {
    return Product.builder()
        .project(project)
        .productDescription(productInfoForProjectRequestDto.getProductDescription())
        .productName(productInfoForProjectRequestDto.getProductName())
        .productPrice(productInfoForProjectRequestDto.getProductPrice())
        .productCurrentStockQuantity(
            productInfoForProjectRequestDto.getProductCurrentStockQuantity())
        .productRegisteredStockQuantity(
            productInfoForProjectRequestDto.getProductRegisteredStockQuantity())
        .build();
  }

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

  public RegisteredProjectDetailForStatusResponseDto getStatusOfRegisteredProject(Long projectId) {
    CircuitBreaker circuitebreaker = circuitBreakerFactory.create("circuiteBreaker");
    // 펀딩률 가격 서포터 수 등 - funding feign client
    FundingAchievementResultOfProjectDetailResponseDto fundingAchievementResultOfProjectDetail =
        (FundingAchievementResultOfProjectDetailResponseDto)
            circuitebreaker.run(
                () ->
                    fundingServiceClient.getFundingInformationOfProjectDetail(projectId).getData(),
                trowable -> new FundingServiceClientOutOfServiceException());

    // project name
    Project project =
        projectRepository.findById(projectId).orElseThrow(ProjectEntityNotFoundException::new);

    // product name price current stock
    List<Product> products = productRepository.findByProject(project);
    List<ProductInformationForRegisteredProjectDto> productsDto =
        productMapper.productEntityToProductInformationForRegisteredProjectToList(products);

    return RegisteredProjectDetailForStatusResponseDto.builder()
        .projectName(project.getProjectName())
        .projectDueDate(project.getProjectDueDate())
        .products(productsDto)
        .fundingAchievementRate(fundingAchievementResultOfProjectDetail.getFundingAchievementRate())
        .accumulatedFundingAmount(
            fundingAchievementResultOfProjectDetail.getAccumulatedFundingAmount())
        .numberOfBuyers(fundingAchievementResultOfProjectDetail.getNumberOfBuyers())
        .remainDate(Duration.between(LocalDateTime.now(), project.getProjectDueDate()).toDays())
        .build();
  }

  public ProjectRegisteredByMakerResponseDto getRegisteredProject(
      Long memberId, Pageable pageable) {
    CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");

    // maker name
    Maker maker =
        makerRepository.findByMemberId(memberId).orElseThrow(MakerEntityNotfoundException::new);

    Page<Project> registeredProjects = projectRepository.findByRegisteredProjects(maker, pageable);
    List<Long> projectIds =
        registeredProjects.stream().map(Project::getProjectId).collect(Collectors.toList());

    List<ProjectAmountWithIdDto> projectAmountWithIdDtos =
        registeredProjects.stream()
            .map(
                item ->
                    ProjectAmountWithIdDto.builder()
                        .projectId(item.getProjectId())
                        .targetAmount(item.getProjectTargetAmount())
                        .build())
            .collect(Collectors.toList());

    ProjectAmountWithIdRequestDto amountWithIdRequestDto =
        ProjectAmountWithIdRequestDto.builder()
            .projectAmountWithIdDtos(projectAmountWithIdDtos)
            .build();

    // 프로젝트 id를 key로 하는 펀딩 달성률, 누적 펀딩 금액 데이터
    FundingAchievementResultMapResponseDto projectStatisticDtos =
        (FundingAchievementResultMapResponseDto)
            circuitBreaker.run(
                () -> fundingServiceClient.getRegisteredProject(amountWithIdRequestDto).getData(),
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
                        .isAuthorized(project.getProjectIsAuthorized())
                        .build()));

    return ProjectRegisteredByMakerResponseDto.builder().projects(projectDtoList).build();
  }

  @Transactional
  public void createProject(
      Long memberId, ProjectRegisterInformationRequestDto projectRegisterInformationDto) {

    // 메이커 저장
    MakerRegisterRequestDto makerRegisterRequestDto = projectRegisterInformationDto.getMaker();
    Maker maker = makerMapper.makerRegisterRequestDtoToEntity(makerRegisterRequestDto);
    maker.setMemberId(memberId);

    // category
    Category category =
        categoryRepository
            .findByCategoryId(projectRegisterInformationDto.getCategoryId())
            .orElseThrow(CategoryNotfoundException::new);

    // 프로젝트 저장
    Project project =
        Project.builder()
            .projectName(projectRegisterInformationDto.getProjectName())
            .projectDescription(projectRegisterInformationDto.getProjectDescription())
            .projectStoryImageUrl(projectRegisterInformationDto.getProjectStoryImageUrl())
            .projectTag(projectRegisterInformationDto.getProjectTag())
            .projectTargetAmount(projectRegisterInformationDto.getProjectTargetAmount())
            .projectDueDate(projectRegisterInformationDto.getProjectDueDate())
            .category(category)
            .maker(maker)
            .build();

    // 프로젝트이미지 저장 -
    project
        .getProjectImages()
        .add(
            getProjectImage(
                project, projectRegisterInformationDto.getProjectThumbnailImageUrl(), true));
    projectRegisterInformationDto
        .getProjectImages()
        .forEach(image -> project.getProjectImages().add(getProjectImage(project, image, false)));

    List<ProductInfoForProjectRequestDto> products = projectRegisterInformationDto.getProducts();
    products.forEach(
        product -> project.getProducts().add(getProduct(project, product))); // product list 객체

    if (projectRegisterInformationDto.isLotdeal()) {
      // 롯딜 저장
      Lotdeal lotdeal = Lotdeal.builder().project(project).build();
      lotdealRepository.save(lotdeal);
    }
    // 저장
    Maker savedMaker = makerRepository.save(maker);
    makerProducer.sendCreateMaker(makerMapper.makerEntityToCreateMakerRequestDto(savedMaker));
    projectProducer.sendCreateProject(
        projectMapper.projectEntityToCreateProjectRequestDto(project));
  }
}
