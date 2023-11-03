package com.lotdiz.projectservice.entity;

import com.lotdiz.projectservice.entity.common.BaseEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
@Table(name = "project")
public class Project extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "project_id")
  private Long projectId;

  @JoinColumn(name = "maker_id")
  @ManyToOne(targetEntity = Maker.class, fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  private Maker maker;

  @JoinColumn(name = "category_id")
  @ManyToOne(targetEntity = Category.class, fetch = FetchType.LAZY)
  private Category category;

  @Column(name = "project_name", nullable = false)
  private String projectName;

  @Column(name = "project_description", nullable = false)
  private String projectDescription;

  @Column(name = "project_tag", nullable = false)
  private String projectTag;

  @Column(name = "project_target_amount", nullable = false)
  private Long projectTargetAmount;

  @Builder.Default
  @Enumerated(EnumType.STRING)
  @Column(
      name = "project_status",
      nullable = false,
      columnDefinition = "VARCHAR(255) DEFAULT 'PENDING'")
  private ProjectStatus projectStatus = ProjectStatus.PENDING;

  @Column(name = "project_story_image_url", nullable = false)
  private String projectStoryImageUrl;

  @Builder.Default
  @Column(name = "project_is_authorized", nullable = false)
  private Boolean projectIsAuthorized = false;

  @Column(name = "project_due_date", nullable = false)
  private LocalDateTime projectDueDate;

  @Builder.Default
  @OneToMany(
      mappedBy = "project",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
      orphanRemoval = true)
  private List<ProjectImage> projectImages = new ArrayList<>();

  @Builder.Default
  @OneToMany(
      mappedBy = "project",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
      orphanRemoval = true)
  private List<Product> products = new ArrayList<>();

  @OneToOne(
      mappedBy = "project",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  private Lotdeal lotdeal;

  public void setProjectStatus(ProjectStatus projectStatus) {
    this.projectStatus = projectStatus;
  }

  public void setProjectIsAuthorized(Boolean projectIsAuthorized) {
    this.projectIsAuthorized = projectIsAuthorized;
  }

  public void setLotdeal(Lotdeal lotdeal) {
    this.lotdeal = lotdeal;
  }

  public void setMaker(Maker maker) {
    this.maker = maker;
  }
}
