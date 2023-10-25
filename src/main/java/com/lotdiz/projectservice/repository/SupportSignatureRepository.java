package com.lotdiz.projectservice.repository;

import com.lotdiz.projectservice.entity.Project;
import com.lotdiz.projectservice.entity.SupportSignature;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SupportSignatureRepository extends JpaRepository<SupportSignature, Long> {
  Long countByProject(Project project);
  Boolean existsByProjectAndMemberId(Project project, Long memberId);
  Page<SupportSignature> findByProject(Project project, Pageable pageable);
  Optional<SupportSignature> findByProjectAndMemberId(Project project, Long memberId);
}
