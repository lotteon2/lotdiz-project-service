package com.lotdiz.projectservice.entity;

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

  @Test
  void categoryCreate() {

    Category category = Category.builder().categoryName("의류").build();

    em.persist(category);
    em.flush();
    em.clear();

    Category findCategory = em.find(Category.class, 1L);
    Assertions.assertThat(findCategory.getCategoryId()).isSameAs(category.getCategoryId());
  }

  @Test
  void projectCreate() {

    Category category = Category.builder().categoryName("식품").build();
    em.persist(category);
    Category findCategory = em.find(Category.class, 1L);
    LocalDateTime dueDate = LocalDateTime.of(2023, 12, 11, 23, 59, 59);

    Project project =
        Project.builder()
            .category(findCategory)
            .projectName("맛있는딸기")
            .projectDescription("정말 맛있는 딸기")
            .projectTag("딸기")
            .projectTargetAmount(10000000L)
            //            .projectStatus(ProjectStatus.PROCESSING)
            .projectStoryImageUrl("/image")
            //            .projectIsAuthorized(true)
            .projectDueDate(dueDate)
            .build();

    em.persist(project);
    em.flush();
    em.clear();

    Project findProject = em.find(Project.class, 1L);
    Assertions.assertThat(findProject.getProjectId()).isSameAs(project.getProjectId());
  }

  @Test
  void lotdealCreate() {

    Category category = Category.builder().categoryName("식품").build();
    em.persist(category);
    Category findCategory = em.find(Category.class, 1L);
    LocalDateTime dueDate = LocalDateTime.of(2023, 12, 11, 23, 59, 59);

    Project project =
        Project.builder()
            .category(findCategory)
            .projectName("맛있는딸기")
            .projectDescription("정말 맛있는 딸기")
            .projectTag("딸기")
            .projectTargetAmount(10000000L)
            .projectStatus(ProjectStatus.PROCESSING)
            .projectStoryImageUrl("/image")
            .projectIsAuthorized(true)
            .projectDueDate(dueDate)
            .build();

    em.persist(project);
    Project findProject = em.find(Project.class, 1L);

    LocalDateTime startTime = LocalDateTime.of(2023, 11, 11, 0, 0, 0);
    LocalDateTime dueTime = LocalDateTime.of(2023, 11, 11, 1, 0, 0);
    Lotdeal lotdeal =
        Lotdeal.builder()
            .project(findProject)
//            .lotdealStartTime(startTime)
//            .lotdealDueTime(dueTime)
            .build();

    em.persist(lotdeal);
    em.flush();
    em.clear();

    Lotdeal findLotdeal = em.find(Lotdeal.class, 1L);

    Assertions.assertThat(findLotdeal.getLotdealId()).isEqualTo(lotdeal.getLotdealId());
  }

  @Test
  void makerCreate() {

    Maker maker =
        Maker.builder()
            .memberId(1L)
            .makerName("최소영")
            .makerEmail("cso1@naver.com")
            .makerPhoneNumber("01033334444")
            .makerKakaoUrl("kakao-url")
            .makerSnsUrl("sns-url")
            .makerHomeUrl("home-url")
            .build();

    Maker makerNonUrl =
        Maker.builder()
            .memberId(2L)
            .makerName("소소영")
            .makerEmail("cso1@naver.com")
            .makerPhoneNumber("01033334444")
            .build();

    em.persist(maker);
    em.persist(makerNonUrl);
    em.flush();
    em.clear();

    Maker findMaker = em.find(Maker.class, 1L);
    Maker findMakerNonUrl = em.find(Maker.class, 2L);

    Assertions.assertThat(findMaker.getMakerId()).isSameAs(maker.getMakerId());
    Assertions.assertThat(findMakerNonUrl.getMakerId()).isSameAs(makerNonUrl.getMakerId());
  }

  @Test
  void productCreate() {

    Category category = Category.builder().categoryName("식품").build();
    em.persist(category);
    Category findCategory = em.find(Category.class, 1L);
    LocalDateTime dueDate = LocalDateTime.of(2023, 12, 11, 23, 59, 59);

    Project project =
        Project.builder()
            .category(findCategory)
            .projectName("맛있는딸기")
            .projectDescription("정말 맛있는 딸기")
            .projectTag("딸기")
            .projectTargetAmount(10000000L)
            .projectStatus(ProjectStatus.PROCESSING)
            .projectStoryImageUrl("/image")
            .projectIsAuthorized(true)
            .projectDueDate(dueDate)
            .build();

    em.persist(project);
    Project findProject = em.find(Project.class, 1L);

    Product product =
        Product.builder()
            .project(findProject)
            .productName("딸기 한 박스")
            .productDescription("너무나도 맛있어요!")
            .productPrice(30000L)
            .productRegisteredStockQuantity(30L)
            .productCurrentStockQuantity(20L)
            .build();

    em.persist(product);
    em.flush();
    em.clear();

    Product findProduct = em.find(Product.class, 1L);
    Assertions.assertThat(findProduct.getProductId()).isEqualTo(product.getProductId());
  }

  @Test
  void projectImageCreate() {

    Category category = Category.builder().categoryName("식품").build();
    em.persist(category);
    Category findCategory = em.find(Category.class, 1L);
    LocalDateTime dueDate = LocalDateTime.of(2023, 12, 11, 23, 59, 59);

    Project project =
        Project.builder()
            .category(findCategory)
            .projectName("맛있는딸기")
            .projectDescription("정말 맛있는 딸기")
            .projectTag("딸기")
            .projectTargetAmount(10000000L)
            .projectStatus(ProjectStatus.PROCESSING)
            .projectStoryImageUrl("/image")
            .projectIsAuthorized(true)
            .projectDueDate(dueDate)
            .build();

    em.persist(project);
    Project findProject = em.find(Project.class, 1L);

    ProjectImage projectImage =
        ProjectImage.builder().project(findProject).projectImageUrl("/image").build();

    em.persist(projectImage);
    em.flush();
    em.clear();

    ProjectImage findProjectImage = em.find(ProjectImage.class, 1L);
    Assertions.assertThat(findProjectImage.getProjectImageId())
        .isSameAs(projectImage.getProjectImageId());
  }

  @Test
  void supportSignatureCreate() {

    Category category = Category.builder().categoryName("식품").build();
    em.persist(category);
    Category findCategory = em.find(Category.class, 1L);
    LocalDateTime dueDate = LocalDateTime.of(2023, 12, 11, 23, 59, 59);

    Project project =
        Project.builder()
            .category(findCategory)
            .projectName("맛있는딸기")
            .projectDescription("정말 맛있는 딸기")
            .projectTag("딸기")
            .projectTargetAmount(10000000L)
            .projectStatus(ProjectStatus.PROCESSING)
            .projectStoryImageUrl("/image")
            .projectIsAuthorized(true)
            .projectDueDate(dueDate)
            .build();

    em.persist(project);
    Project findProject = em.find(Project.class, 1L);

    SupportSignature supportSignature =
        SupportSignature.builder()
            .memberId(1L)
            .project(findProject)
            .supportSignatureContent("정말 기대되는 펀딩입니다.!!!!")
            .build();
    em.persist(supportSignature);
    em.flush();
    em.clear();

    SupportSignature findSupportSignature = em.find(SupportSignature.class, 1L);
    Assertions.assertThat(findSupportSignature.getSupportSignatureId())
        .isSameAs(supportSignature.getSupportSignatureId());
  }

  @Test
  void bannerCreate() {

    Banner banner =
        Banner.builder().bannerImageUrl("/banner-image-url").bannerUrl("/banner-url").build();

    em.persist(banner);
    em.flush();
    em.clear();

    Banner findBanner = em.find(Banner.class, 1L);
    Assertions.assertThat(findBanner.getBannerId()).isSameAs(banner.getBannerId());

  }
}
