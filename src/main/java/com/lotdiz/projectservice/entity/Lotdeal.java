package com.lotdiz.projectservice.entity;

import com.lotdiz.projectservice.entity.common.BaseEntity;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
@Table(name = "lotdeal")
public class Lotdeal extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "lotdeal_id")
  private Long lotdealId;

  @JoinColumn(name = "project_id")
  @OneToOne(
      targetEntity = Project.class,
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  private Project project;

  @Column(name = "lotdeal_start_time")
  private LocalDateTime lotdealStartTime;

  @Column(name = "lotdeal_due_time")
  private LocalDateTime lotdealDueTime;
}
