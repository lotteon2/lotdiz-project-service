package com.lotdiz.projectservice.entity;

import com.lotdiz.projectservice.entity.common.BaseEntity;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
@Table(name="project_image")
public class ProjectImage extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_image_id")
    private Long projectImageId;

    @JoinColumn(name = "project_id")
    @ManyToOne(targetEntity = Project.class, fetch= FetchType.LAZY)
    private Project project;

    @Column(name = "project_image_url", nullable = false)
    private String projectImageUrl;

    @Builder.Default
    @Column(name = "project_image_is_thumbnail", nullable = false)
    private Boolean projectImageIsThumbnail = false;

}
