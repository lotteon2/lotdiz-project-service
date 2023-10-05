package com.lotdiz.projectservice.entity;

import com.lotdiz.projectservice.entity.common.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
@Table(name="project")
public class Project extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long projectId;

    @JoinColumn(name = "maker_id")
    @ManyToOne(targetEntity = Maker.class, fetch = FetchType.LAZY)
    private Maker maker;

    @ManyToOne(targetEntity = Category.class, fetch=FetchType.LAZY)
    @JoinColumn(name = "category_id")
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
    @Column(name = "project_status", nullable = false,
            columnDefinition = "VARCHAR(255) DEFAULT 'PENDING'")
    private ProjectStatus projectStatus = ProjectStatus.PENDING;

    @Column(name = "project_story_image_url", nullable = false)
    private String projectStoryImageUrl;

    @Builder.Default
    @Column(name = "project_is_authorized", nullable = false)
    private Boolean projectIsAuthorized = false;

    @Column(name = "project_due_date", nullable = false)
    private LocalDateTime projectDueDate;

}
