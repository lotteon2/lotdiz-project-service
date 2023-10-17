package com.lotdiz.projectservice.service;

import com.lotdiz.projectservice.client.DeliveryServiceClient;
import com.lotdiz.projectservice.client.FundingServiceClient;
import com.lotdiz.projectservice.client.MemberServiceClient;
import com.lotdiz.projectservice.dto.DeliveryStatusOfFundingDto;
import com.lotdiz.projectservice.dto.MemberFundingInformationDto;
import com.lotdiz.projectservice.dto.MemberNameDto;
import com.lotdiz.projectservice.dto.RegisteredProjectFundingDto;
import com.lotdiz.projectservice.dto.request.MakerRegisterRequestDto;
import com.lotdiz.projectservice.dto.request.ProductInfoForProjectRequestDto;
import com.lotdiz.projectservice.dto.request.ProjectRegisterInformationRequestDto;
import com.lotdiz.projectservice.dto.response.DeliveryStatusResponseDto;
import com.lotdiz.projectservice.dto.response.MemberInformationOfFundingResponseDto;
import com.lotdiz.projectservice.dto.response.MemberNameResponseDto;
import com.lotdiz.projectservice.dto.response.RegisteredProjectFundingListResponseDto;
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
import com.lotdiz.projectservice.mapper.MakerMapper;
import com.lotdiz.projectservice.repository.CategoryRepository;
import com.lotdiz.projectservice.repository.LotdealRepository;
import com.lotdiz.projectservice.repository.MakerRepository;
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
  private final CategoryRepository categoryRepository;
  private final MakerRepository makerRepository;
  private final LotdealRepository lotdealRepository;
  private final MakerMapper makerMapper;

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
  }
}
