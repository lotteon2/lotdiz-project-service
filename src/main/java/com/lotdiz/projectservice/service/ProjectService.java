package com.lotdiz.projectservice.service;

import com.lotdiz.projectservice.dto.request.MakerRegisterRequestDto;
import com.lotdiz.projectservice.dto.request.ProductInfoForProjectRequestDto;
import com.lotdiz.projectservice.dto.request.ProjectRegisterInformationRequestDto;
import com.lotdiz.projectservice.entity.Category;
import com.lotdiz.projectservice.entity.Lotdeal;
import com.lotdiz.projectservice.entity.Maker;
import com.lotdiz.projectservice.entity.Product;
import com.lotdiz.projectservice.entity.Project;
import com.lotdiz.projectservice.entity.ProjectImage;
import com.lotdiz.projectservice.entity.ProjectStatus;
import com.lotdiz.projectservice.exception.CategoryNotfoundException;
import com.lotdiz.projectservice.mapper.MakerMapper;
import com.lotdiz.projectservice.repository.CategoryRepository;
import com.lotdiz.projectservice.repository.LotdealRepository;
import com.lotdiz.projectservice.repository.MakerRepository;
import com.lotdiz.projectservice.repository.ProjectRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectService {

  private final ProjectRepository projectRepository;
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
