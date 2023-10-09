package com.lotdiz.projectservice.service;

import com.lotdiz.projectservice.dto.ProductDto;
import com.lotdiz.projectservice.dto.ProjectImageDto;
import com.lotdiz.projectservice.dto.response.ProjectByCategoryResponseDto;
import com.lotdiz.projectservice.dto.response.ProjectDetailResponseDto;
import com.lotdiz.projectservice.entity.Project;
import com.lotdiz.projectservice.exception.ProjectEntityNotFoundException;
import com.lotdiz.projectservice.repository.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectForSupporterService {

  private final ProjectRepository projectRepository;
  private final LotdealRepository lotdealRepository;
  private final ProjectImageRepository projectImageRepository;
  private final ProductRepository productRepository;
  private final SupportSignatureRepository supportSignatureRepository;

  @Transactional(readOnly = true)
  public List<ProjectByCategoryResponseDto> getProjectsByCategory(
      String categoryName, Pageable pageable) {

    List<ProjectByCategoryResponseDto> projectByCategoryResponseDtoList = new ArrayList<>();

    Page<Project> projects =
        projectRepository
            .findByCategoryAndProjectIsAuthorized(categoryName, true, pageable)
            .orElseThrow(ProjectEntityNotFoundException::new);

    // TODO project left outer join lotdeal  ?
    for (Project p : projects) {
      ProjectByCategoryResponseDto projectByCategoryResponseDto =
          lotdealRepository
              .findByProjectAndLotdealing(p, LocalDateTime.now())
              .map(l -> ProjectByCategoryResponseDto.fromProjectEntity(p, l.getLotdealDueTime()))
              .orElseGet(() -> ProjectByCategoryResponseDto.fromProjectEntity(p, null));

      projectByCategoryResponseDtoList.add(projectByCategoryResponseDto);
    }





    return projectByCategoryResponseDtoList;
  }

  @Transactional(readOnly = true)
  public ProjectDetailResponseDto getProjectDetails(Long projectId) {

    Project project =
        projectRepository.findById(projectId).orElseThrow(ProjectEntityNotFoundException::new);

    List<ProjectImageDto> projectImageDtoList =
        projectImageRepository.findByProject(project).stream()
            .flatMap(Collection::stream)
            .map(projectImage -> ProjectImageDto.fromProjectImageEntity(projectImage))
            .collect(Collectors.toList());

    List<ProductDto> productDtoList =
        productRepository.findByProject(project).stream()
            .flatMap(Collection::stream)
            .map(product -> ProductDto.fromProductEntity(product))
            .collect(Collectors.toList());

    Long numberOfSupporter = supportSignatureRepository.countByProject(project);

    ProjectDetailResponseDto projectDetailResponseDto =
        lotdealRepository
            .findByProjectAndLotdealing(project, LocalDateTime.now())
            .map(
                l ->
                    ProjectDetailResponseDto.fromProjectEntity(
                        project,
                        projectImageDtoList,
                        productDtoList,
                        numberOfSupporter,
                        l.getLotdealDueTime()))
            .orElseGet(
                () ->
                    ProjectDetailResponseDto.fromProjectEntity(
                        project, projectImageDtoList, productDtoList, numberOfSupporter, null));

    return projectDetailResponseDto;
  }
}
