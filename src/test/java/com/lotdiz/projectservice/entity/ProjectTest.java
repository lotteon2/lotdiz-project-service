package com.lotdiz.projectservice.entity;

import com.lotdiz.projectservice.repository.*;
import java.time.LocalDateTime;
import javax.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProjectTest {

  @Autowired private EntityManager em;
  @Autowired private BannerRepository bannerRepository;
  @Autowired private CategoryRepository categoryRepository;
  @Autowired private LotdealRepository lotdealRepository;
  @Autowired private MakerRepository makerRepository;
  @Autowired private ProductRepository productRepository;
  @Autowired private ProjectImageRepository projectImageRepository;
  @Autowired private ProjectRepository projectRepository;
  @Autowired private SupportSignatureRepository supportSignatureRepository;

  @Test
  void categoryCreate() {

    Category categoryEntity = Category.builder().categoryName("의류").build();
    Category category = categoryRepository.save(categoryEntity);
    em.flush();
    em.clear();
    Category findCategory = categoryRepository.findById(category.getCategoryId()).get();
    Assertions.assertThat(findCategory.getCategoryId()).isSameAs(category.getCategoryId());
  }

  @Test
  void projectCreate() {

    LocalDateTime dueDate = LocalDateTime.of(2023, 12, 11, 23, 59, 59);

    Category category = Category.builder().categoryName("식품").build();
    categoryRepository.save(category);

    Maker maker =
        Maker.builder()
            .memberId(1L)
            .makerName("최소영")
            .makerEmail("cso1@naver.com")
            .makerPhoneNumber("01044445555")
            .build();
    makerRepository.save(maker);

    Project projectEntity =
        Project.builder()
            .category(category)
            .maker(maker)
            .projectName("맛있는딸기")
            .projectDescription("정말 맛있는 딸기")
            .projectTag("딸기")
            .projectTargetAmount(10000000L)
            .projectStoryImageUrl("/image")
            .projectDueDate(dueDate)
            .build();
    Project project = projectRepository.save(projectEntity);
    em.flush();
    em.clear();
    Project findProject = projectRepository.findById(project.getProjectId()).get();
    Assertions.assertThat(findProject.getProjectId()).isSameAs(project.getProjectId());
  }

  @Test
  void lotdealCreate() {

    LocalDateTime dueDate = LocalDateTime.of(2023, 12, 11, 23, 59, 59);

    Category category = Category.builder().categoryName("식품").build();
    categoryRepository.save(category);

    Maker maker =
        Maker.builder()
            .memberId(1L)
            .makerName("최소영")
            .makerEmail("cso1@naver.com")
            .makerPhoneNumber("01044445555")
            .build();
    makerRepository.save(maker);

    Project project =
        Project.builder()
            .category(category)
            .maker(maker)
            .projectName("맛있는딸기")
            .projectDescription("정말 맛있는 딸기")
            .projectTag("딸기")
            .projectTargetAmount(10000000L)
            .projectStoryImageUrl("/image")
            .projectDueDate(dueDate)
            .build();
    projectRepository.save(project);

    Lotdeal lotdealEntity = Lotdeal.builder().project(project).build();
    Lotdeal lotdeal = lotdealRepository.save(lotdealEntity);

    em.flush();
    em.clear();
    Lotdeal findLotdeal = lotdealRepository.findById(lotdeal.getLotdealId()).get();

    Assertions.assertThat(findLotdeal.getLotdealId()).isEqualTo(lotdeal.getLotdealId());
  }

  @Test
  void makerCreate() {

    Maker makerEntity =
        Maker.builder()
            .memberId(1L)
            .makerName("최소영")
            .makerEmail("cso1@naver.com")
            .makerPhoneNumber("01033334444")
            .makerKakaoUrl("kakao-url")
            .makerSnsUrl("sns-url")
            .makerHomeUrl("home-url")
            .build();
    Maker maker = makerRepository.save(makerEntity);

    em.flush();
    em.clear();
    Maker findMaker = makerRepository.findById(maker.getMakerId()).get();

    Assertions.assertThat(findMaker.getMakerId()).isSameAs(maker.getMakerId());
  }

  @Test
  void productCreate() {

    LocalDateTime dueDate = LocalDateTime.of(2023, 12, 11, 23, 59, 59);

    Category category = Category.builder().categoryName("식품").build();
    categoryRepository.save(category);

    Maker maker =
        Maker.builder()
            .memberId(1L)
            .makerName("최소영")
            .makerEmail("cso1@naver.com")
            .makerPhoneNumber("01044445555")
            .build();
    makerRepository.save(maker);

    Project project =
        Project.builder()
            .category(category)
            .maker(maker)
            .projectName("맛있는딸기")
            .projectDescription("정말 맛있는 딸기")
            .projectTag("딸기")
            .projectTargetAmount(10000000L)
            .projectStoryImageUrl("/image")
            .projectDueDate(dueDate)
            .build();
    projectRepository.save(project);

    Product productEntity =
        Product.builder()
            .project(project)
            .productName("딸기 한 박스")
            .productDescription("너무나도 맛있어요!")
            .productPrice(30000L)
            .productRegisteredStockQuantity(30L)
            .productCurrentStockQuantity(20L)
            .build();
    Product product = productRepository.save(productEntity);

    em.flush();
    em.clear();
    Product findProduct = productRepository.findById(product.getProductId()).get();

    Assertions.assertThat(findProduct.getProductId()).isEqualTo(product.getProductId());
  }

  @Test
  void projectImageCreate() {

    LocalDateTime dueDate = LocalDateTime.of(2023, 12, 11, 23, 59, 59);

    Category category = Category.builder().categoryName("식품").build();
    categoryRepository.save(category);

    Maker maker =
        Maker.builder()
            .memberId(1L)
            .makerName("최소영")
            .makerEmail("cso1@naver.com")
            .makerPhoneNumber("01044445555")
            .build();
    makerRepository.save(maker);

    Project project =
        Project.builder()
            .category(category)
            .maker(maker)
            .projectName("맛있는딸기")
            .projectDescription("정말 맛있는 딸기")
            .projectTag("딸기")
            .projectTargetAmount(10000000L)
            .projectStoryImageUrl("/image")
            .projectDueDate(dueDate)
            .build();
    projectRepository.save(project);

    ProjectImage projectImageEntity =
        ProjectImage.builder().project(project).projectImageUrl("/image").build();
    ProjectImage projectImage = projectImageRepository.save(projectImageEntity);

    em.flush();
    em.clear();
    ProjectImage findProjectImage =
        projectImageRepository.findById(projectImage.getProjectImageId()).get();

    Assertions.assertThat(findProjectImage.getProjectImageId())
        .isSameAs(projectImage.getProjectImageId());
  }

  @Test
  void supportSignatureCreate() {

    LocalDateTime dueDate = LocalDateTime.of(2023, 12, 11, 23, 59, 59);

    Category category = Category.builder().categoryName("식품").build();
    categoryRepository.save(category);

    Maker maker =
        Maker.builder()
            .memberId(1L)
            .makerName("최소영")
            .makerEmail("cso1@naver.com")
            .makerPhoneNumber("01044445555")
            .build();
    makerRepository.save(maker);

    Project project =
        Project.builder()
            .category(category)
            .maker(maker)
            .projectName("맛있는딸기")
            .projectDescription("정말 맛있는 딸기")
            .projectTag("딸기")
            .projectTargetAmount(10000000L)
            .projectStoryImageUrl("/image")
            .projectDueDate(dueDate)
            .build();
    projectRepository.save(project);

    SupportSignature supportSignatureEntity =
        SupportSignature.builder()
            .memberId(1L)
            .project(project)
            .supportSignatureContent("정말 기대되는 펀딩입니다.!!!!")
            .build();
    SupportSignature supportSignature = supportSignatureRepository.save(supportSignatureEntity);

    em.flush();
    em.clear();
    SupportSignature findSupportSignature =
        supportSignatureRepository.findById(supportSignature.getSupportSignatureId()).get();
    Assertions.assertThat(findSupportSignature.getSupportSignatureId())
        .isSameAs(supportSignature.getSupportSignatureId());
  }

  @Test
  void bannerCreate() {

    Banner bannerEntity =
        Banner.builder().bannerImageUrl("/banner-image-url").bannerUrl("/banner-url").build();
    Banner banner = bannerRepository.save(bannerEntity);

    em.flush();
    em.clear();
    Banner findBanner = bannerRepository.findById(banner.getBannerId()).get();

    Assertions.assertThat(findBanner.getBannerId()).isSameAs(banner.getBannerId());
  }
}
