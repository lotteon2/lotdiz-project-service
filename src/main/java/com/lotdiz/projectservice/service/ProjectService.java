package com.lotdiz.projectservice.service;

import com.lotdiz.projectservice.client.FundingServiceClient;
import com.lotdiz.projectservice.dto.ProductInformationForRegisteredProjectDto;
import com.lotdiz.projectservice.dto.response.FundingAchievementResultOfProjectDetailResponseDto;
import com.lotdiz.projectservice.dto.response.RegisteredProjectDetailForStatusResponseDto;
import com.lotdiz.projectservice.entity.Product;
import com.lotdiz.projectservice.entity.Project;
import com.lotdiz.projectservice.entity.ProjectStatus;
import com.lotdiz.projectservice.exception.FundingServiceClientOutOfServiceException;
import com.lotdiz.projectservice.exception.ProjectEntityNotFoundException;
import com.lotdiz.projectservice.mapper.ProductMapper;
import com.lotdiz.projectservice.repository.ProductRepository;
import com.lotdiz.projectservice.repository.ProjectRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
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
  private final ProductRepository productRepository;
  private final CircuitBreakerFactory circuitBreakerFactory;
  private final ProductMapper productMapper;

  @Transactional
  public int updateProjectStatusDueDateAfter(ProjectStatus projectStatus, List<Long> projectIds) {
    return projectRepository.updateProjectStatusDueDateAfter(
        projectStatus, LocalDateTime.now(), projectIds);
  }

  public RegisteredProjectDetailForStatusResponseDto getStatusOfRegisteredProject(Long projectId) {
    CircuitBreaker circuitebreaker = circuitBreakerFactory.create("circuiteBreaker");
    // 펀딩률 가격 서포터 수 등 - funding feign client
    FundingAchievementResultOfProjectDetailResponseDto fundingAchievementResultOfProjectDetail =
        (FundingAchievementResultOfProjectDetailResponseDto)
            circuitebreaker.run(
                () -> fundingServiceClient.getFundingOfProjectDetail(projectId).getData(),
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
}
