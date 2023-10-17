package com.lotdiz.projectservice.controller.restcontroller;

import com.lotdiz.projectservice.dto.request.ProjectRegisterInformationRequestDto;
import com.lotdiz.projectservice.dto.request.SupportSignatureRequestDto;
import com.lotdiz.projectservice.dto.response.*;
import com.lotdiz.projectservice.dto.response.ProjectByCategoryResponseDto;
import com.lotdiz.projectservice.dto.response.ProjectDetailResponseDto;
import com.lotdiz.projectservice.service.ProjectForSupporterService;
import com.lotdiz.projectservice.service.ProjectService;
import com.lotdiz.projectservice.utils.SuccessResponse;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProjectRestController {

  private final ProjectForSupporterService projectForSupporterService;
  private final ProjectService projectService;

  @GetMapping("/projects/category/{categoryName}")
  public ResponseEntity<SuccessResponse<Map<String, List<ProjectByCategoryResponseDto>>>>
      getProjectsByCategory(
          @RequestHeader(required = false) Long memberId,
          @PathVariable String categoryName,
          @PageableDefault(
                  page = 0,
                  size = 20,
                  sort = {"createdAt"},
                  direction = Sort.Direction.DESC)
              Pageable pageable) {

    List<ProjectByCategoryResponseDto> projectByCategoryResponseDtoList =
        projectForSupporterService.getProjectsByCategory(categoryName, pageable, memberId);

    return ResponseEntity.ok()
        .body(
            SuccessResponse.<Map<String, List<ProjectByCategoryResponseDto>>>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.name())
                .detail("카테고리 별, 프로젝트 조회 성공")
                .data(Map.of("projects", projectByCategoryResponseDtoList))
                .build());
  }

  @GetMapping("/projects/{projectId}")
  public ResponseEntity<SuccessResponse<Map<String, ProjectDetailResponseDto>>> getProjectDetails(
      @PathVariable Long projectId) {

    ProjectDetailResponseDto projectDetailResponseDto =
        projectForSupporterService.getProjectDetails(projectId);

    return ResponseEntity.ok()
        .body(
            SuccessResponse.<Map<String, ProjectDetailResponseDto>>builder()
                .code(String.valueOf((HttpStatus.OK.value())))
                .message(HttpStatus.OK.name())
                .detail("프로젝트 상세 페이지 조회 성공")
                .data(Map.of("projectDetail", projectDetailResponseDto))
                .build());
  }

  @PostMapping("/projects/{projectId}/support-signature")
  public ResponseEntity<SuccessResponse> createSupportSignature(
      @RequestHeader(required = false) Long memberId,
      @PathVariable Long projectId,
      @Valid @RequestBody SupportSignatureRequestDto supportSignatureContents) {

    projectForSupporterService.createSupportSignature(
        memberId, projectId, supportSignatureContents);

    return ResponseEntity.ok()
        .body(
            SuccessResponse.builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.name())
                .detail("지지서명 작성 성공")
                .build());
  }

  @GetMapping("/projects/{projectId}/support-signature")
  public ResponseEntity<SuccessResponse<Map<String, List<SupportSignatureResponseDto>>>>
      getSupportSignature(
          @PathVariable Long projectId,
          @PageableDefault(
                  page = 0,
                  size = 20,
                  sort = {"createdAt"},
                  direction = Sort.Direction.DESC)
              Pageable pageable) {

    List<SupportSignatureResponseDto> supportSignatureResponseDtoList =
        projectForSupporterService.getSupportSignature(projectId, pageable);

    return ResponseEntity.ok()
        .body(
            SuccessResponse.<Map<String, List<SupportSignatureResponseDto>>>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.name())
                .detail("지지서명 조회 성공")
                .data(Map.of("projectDetail", supportSignatureResponseDtoList))
                .build());
  }

  @PutMapping("/projects/{projectId}/support-signature")
  public ResponseEntity<SuccessResponse> modifySupportSignature(
      @RequestHeader(required = false) Long memberId,
      @PathVariable Long projectId,
      @Valid @RequestBody SupportSignatureRequestDto supportSignatureContents) {

    projectForSupporterService.modifySupportSignature(
        memberId, projectId, supportSignatureContents);

    return ResponseEntity.ok()
        .body(
            SuccessResponse.builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.name())
                .detail("지지서명 수정 성공")
                .build());
  }

  @GetMapping("/projects/lotdeal")
  public ResponseEntity<SuccessResponse<Map<String, List<LotdealProjectResponseDto>>>> getLotdeal(
      @RequestHeader(required = false) Long memberId, Pageable pageable) {

    List<LotdealProjectResponseDto> lotdealProjectResponseDtoList =
        projectForSupporterService.getLotdeal(pageable, memberId);

    return ResponseEntity.ok()
        .body(
            SuccessResponse.<Map<String, List<LotdealProjectResponseDto>>>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.name())
                .detail("롯딜 목록 조회 성공")
                .data(Map.of("lotdealProjects", lotdealProjectResponseDtoList))
                .build());
  }

  @PostMapping("/project/makers/projects")
  public ResponseEntity<SuccessResponse<String>> registerProject(
      @RequestBody ProjectRegisterInformationRequestDto projectRegisterInformationDto,
      @RequestHeader Long memberId) {
    projectService.createProject(memberId, projectRegisterInformationDto);

    return ResponseEntity.ok()
        .body(
            SuccessResponse.<String>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.name())
                .detail("프로젝트가 등록되었습니다!")
                .build());
  }

  @GetMapping("/projects/banner")
  public ResponseEntity<SuccessResponse<Map<String, List<BannerResponseDto>>>> getBanners() {

    List<BannerResponseDto> bannerResponseDtoList = projectForSupporterService.getBanners();

    return ResponseEntity.ok()
        .body(
            SuccessResponse.<Map<String, List<BannerResponseDto>>>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.name())
                .detail("배너 조회 성공")
                .data(Map.of("banners", bannerResponseDtoList))
                .build());
  }

  @GetMapping("/projects/special-exhibition")
  public ResponseEntity<SuccessResponse<Map<String, List<SpecialExhibitionResponseDto>>>>
      getSpecialExhibition(
          @RequestHeader Long memberId,
          @RequestParam String tag,
          @PageableDefault(
                  page = 0,
                  size = 20,
                  sort = {"createdAt"},
                  direction = Sort.Direction.DESC)
              Pageable pageable) {

    List<SpecialExhibitionResponseDto> specialExhibitionResponseDtoList =
        projectForSupporterService.getSpecialExhibition(pageable, tag, memberId);

    return ResponseEntity.ok()
        .body(
            SuccessResponse.<Map<String, List<SpecialExhibitionResponseDto>>>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.name())
                .detail("기획전 조회 성공")
                .data(Map.of("special-exhibition", specialExhibitionResponseDtoList))
                .build());
  }
}
