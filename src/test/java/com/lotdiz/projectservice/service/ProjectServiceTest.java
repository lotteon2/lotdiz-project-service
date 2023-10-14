package com.lotdiz.projectservice.service;

import static org.junit.jupiter.api.Assertions.*;

import com.lotdiz.projectservice.dto.request.MakerRegisterRequestDto;
import com.lotdiz.projectservice.dto.request.ProductInfoForProjectRequestDto;
import com.lotdiz.projectservice.dto.request.ProjectRegisterInformationRequestDto;
import com.lotdiz.projectservice.entity.Category;
import com.lotdiz.projectservice.entity.Lotdeal;
import com.lotdiz.projectservice.entity.Maker;
import com.lotdiz.projectservice.entity.Product;
import com.lotdiz.projectservice.entity.Project;
import com.lotdiz.projectservice.entity.ProjectImage;
import com.lotdiz.projectservice.exception.CategoryNotfoundException;
import com.lotdiz.projectservice.mapper.MakerMapperImpl;
import com.lotdiz.projectservice.repository.CategoryRepository;
import com.lotdiz.projectservice.repository.LotdealRepository;
import com.lotdiz.projectservice.repository.MakerRepository;
import com.lotdiz.projectservice.repository.ProjectRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class ProjectServiceTest {
  @Autowired private CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);
  @Autowired private LotdealRepository lotdealRepository = Mockito.mock(LotdealRepository.class);
  @Autowired private MakerRepository makerRepository = Mockito.mock(MakerRepository.class);
  @Autowired private ProjectRepository projectRepository = Mockito.mock(ProjectRepository.class);
  @InjectMocks private MakerMapperImpl makerMapper;

  @Test
  void createProject() {
    Long memberId = 1L;
    ProjectRegisterInformationRequestDto projectRegisterInformationRequestDto =
        ProjectRegisterInformationRequestDto.builder()
            .projectName("first project")
            .projectDescription("description of project")
            .projectDueDate(LocalDateTime.now())
            .projectTag("tag")
            .projectTargetAmount(20000L)
            .projectImages(List.of("1url", "2url", "3url"))
            .projectStoryImageUrl("story image")
            .products(
                List.of(
                    ProductInfoForProjectRequestDto.builder()
                        .productName("first product")
                        .productPrice(200L)
                        .productCurrentStockQuantity(300L)
                        .productDescription("description of product")
                        .productRegisteredStockQuantity(300L)
                        .build()))
            .categoryId(1L)
            .isLotdeal(true)
            .maker(
                MakerRegisterRequestDto.builder()
                    .makerEmail("maker eamil")
                    .makerName("maker name ")
                    .makerImageUrl("maker image url ")
                    .contactEmail(" contact eamil")
                    .makerPhoneNumber("101-101")
                    .build())
            .projectThumbnailImageUrl("project thumbnail image")
            .build();

    // 메이커 저장
    MakerRegisterRequestDto makerRegisterRequestDto =
        projectRegisterInformationRequestDto.getMaker();
    Maker maker = makerMapper.makerRegisterRequestDtoToEntity(makerRegisterRequestDto);
    maker.setMakerId(10L);
    maker.setMemberId(memberId);

    // category
    Category category =
        categoryRepository
            .findByCategoryId(projectRegisterInformationRequestDto.getCategoryId())
            .orElseThrow(CategoryNotfoundException::new);

    // 프로젝트 저장
    Project project =
        Project.builder()
            .projectId(10L)
            .projectName(projectRegisterInformationRequestDto.getProjectName())
            .projectDescription(projectRegisterInformationRequestDto.getProjectDescription())
            .projectStoryImageUrl(projectRegisterInformationRequestDto.getProjectStoryImageUrl())
            .projectTag(projectRegisterInformationRequestDto.getProjectTag())
            .projectTargetAmount(projectRegisterInformationRequestDto.getProjectTargetAmount())
            .projectDueDate(projectRegisterInformationRequestDto.getProjectDueDate())
            .category(category)
            .maker(maker)
            .build();

    // 프로젝트이미지 저장 -
    Long projectImageId = 10L;
    project
        .getProjectImages()
        .add(
            ProjectImage.builder()
                .projectImageId(projectImageId + 1L)
                .project(project)
                .projectImageUrl(projectRegisterInformationRequestDto.getProjectThumbnailImageUrl())
                .projectImageIsThumbnail(true)
                .build());
    projectRegisterInformationRequestDto
        .getProjectImages()
        .forEach(
            image ->
                project
                    .getProjectImages()
                    .add(
                        ProjectImage.builder()
                            .projectImageId(projectImageId + 1L)
                            .project(project)
                            .projectImageUrl(image)
                            .projectImageIsThumbnail(true)
                            .build()));

    List<ProductInfoForProjectRequestDto> products =
        projectRegisterInformationRequestDto.getProducts();
    products.forEach(
        product ->
            project
                .getProducts()
                .add(
                    Product.builder()
                        .productId(10L)
                        .project(project)
                        .productDescription(product.getProductDescription())
                        .productName(product.getProductName())
                        .productPrice(product.getProductPrice())
                        .productCurrentStockQuantity(product.getProductCurrentStockQuantity())
                        .productRegisteredStockQuantity(product.getProductRegisteredStockQuantity())
                        .build())); // product list 객체

    // 롯딜 저장 - 따로
    Lotdeal lotdeal = Lotdeal.builder().lotdealId(10L).project(project).build();

    // 저장
    lotdealRepository.save(lotdeal);
    makerRepository.save(maker);
  }
}
