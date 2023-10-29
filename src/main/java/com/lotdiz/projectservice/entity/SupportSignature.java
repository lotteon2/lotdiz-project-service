package com.lotdiz.projectservice.entity;

import com.lotdiz.projectservice.entity.common.BaseEntity;
import javax.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
@Table(name = "support_signature")
public class SupportSignature extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "support_signature_id")
  private Long supportSignatureId;

  @JoinColumn(name = "project_id")
  @ManyToOne(targetEntity = Project.class, fetch = FetchType.LAZY)
  private Project project;

  @Column(name = "member_id", nullable = false)
  private Long memberId;

  @Column(name = "support_signature_content", nullable = false, length = 500)
  private String supportSignatureContent;

  public void setSupportSignatureContent(String supportSignatureContent) {
    this.supportSignatureContent = supportSignatureContent;
  }
}
