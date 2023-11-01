package com.lotdiz.projectservice.controller.restcontroller;

import com.lotdiz.projectservice.dto.request.ImageRequestDto;
import com.lotdiz.projectservice.dto.request.ProjectRegisterInformationRequestDto;
import com.lotdiz.projectservice.dto.request.SupportSignatureRequestDto;
import com.lotdiz.projectservice.dto.response.*;
import com.lotdiz.projectservice.dto.response.ProjectDetailResponseDto;
import com.lotdiz.projectservice.service.PresignedUrlService;
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
  private final PresignedUrlService presignedUrlService;

  @GetMapping("/projects")
  public ResponseEntity<SuccessResponse<Map<String, Object>>> getLotdplusProject(
      @RequestHeader(required = false) Long memberId) {

    PagedDataResponseDto<Object> lotdPlusResponseDtoList =
        projectForSupporterService.getLotdPlusProject(memberId);

    return ResponseEntity.ok()
        .body(
            SuccessResponse.<Map<String, Object>>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.name())
                .detail("메인 페이지 롯드플러스 조회 성공")
                .data(
                    Map.of(
                        "totalPages",
                        lotdPlusResponseDtoList.getTotalPages(),
                        "projects",
                        lotdPlusResponseDtoList.getDataList()))
                .build());
  }

  @GetMapping("/projects/category/{categoryName}")
  public ResponseEntity<SuccessResponse<Map<String, Object>>> getProjectsByCategory(
      @RequestHeader(required = false) Long memberId,
      @PathVariable String categoryName,
      @PageableDefault(
              page = 0,
              size = 20,
              sort = {"createdAt"},
              direction = Sort.Direction.DESC)
          Pageable pageable) {

    PagedDataResponseDto<Object> projectByCategoryResponseDtoList =
        projectForSupporterService.getProjectsByCategory(categoryName, pageable, memberId);

    return ResponseEntity.ok()
        .body(
            SuccessResponse.<Map<String, Object>>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.name())
                .detail("카테고리 별, 프로젝트 조회 성공")
                .data(
                    Map.of(
                        "totalPages",
                        projectByCategoryResponseDtoList.getTotalPages(),
                        "projects",
                        projectByCategoryResponseDtoList.getDataList()))
                .build());
  }

  @GetMapping("/projects/{projectId}")
  public ResponseEntity<SuccessResponse<Map<String, ProjectDetailResponseDto>>> getProjectDetails(
      @RequestHeader(required = false) Long memberId, @PathVariable Long projectId) {

    ProjectDetailResponseDto projectDetailResponseDto =
        projectForSupporterService.getProjectDetails(projectId, memberId);

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
      @RequestHeader Long memberId,
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
  public ResponseEntity<SuccessResponse<Map<String, Object>>> getSupportSignature(
      @PathVariable Long projectId,
      @RequestHeader(required = false) Long memberId,
      @PageableDefault(
              page = 0,
              size = 20,
              sort = {"createdAt"},
              direction = Sort.Direction.DESC)
          Pageable pageable) {

    PagedDataResponseDto<Object> supportSignatureResponseDtoList =
        projectForSupporterService.getSupportSignature(projectId, memberId, pageable);

    return ResponseEntity.ok()
        .body(
            SuccessResponse.<Map<String, Object>>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.name())
                .detail("지지서명 조회 성공")
                .data(
                    Map.of(
                        "totalPages",
                        supportSignatureResponseDtoList.getTotalPages(),
                        "supportSignatures",
                        supportSignatureResponseDtoList.getDataList()))
                .build());
  }

  @PutMapping("/projects/{projectId}/support-signature")
  public ResponseEntity<SuccessResponse> modifySupportSignature(
      @RequestHeader Long memberId,
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
  public ResponseEntity<SuccessResponse<Map<String, Object>>> getLotdeal(
      @RequestHeader(required = false) Long memberId, Pageable pageable) {

    PagedDataResponseDto<Object> lotdealProjectResponseDtoList =
        projectForSupporterService.getLotdeal(pageable, memberId);

    return ResponseEntity.ok()
        .body(
            SuccessResponse.<Map<String, Object>>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.name())
                .detail("롯딜 목록 조회 성공")
                .data(
                    Map.of(
                        "totalPages",
                        lotdealProjectResponseDtoList.getTotalPages(),
                        "projects",
                        lotdealProjectResponseDtoList.getDataList()))
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
  public ResponseEntity<SuccessResponse<Map<String, Object>>> getSpecialExhibition(
      @RequestHeader(required = false) Long memberId,
      @RequestParam String tag,
      @PageableDefault(
              page = 0,
              size = 20,
              sort = {"createdAt"},
              direction = Sort.Direction.DESC)
          Pageable pageable) {

    PagedDataResponseDto<Object> specialExhibitionResponseDtoList =
        projectForSupporterService.getSpecialExhibition(pageable, tag, memberId);

    return ResponseEntity.ok()
        .body(
            SuccessResponse.<Map<String, Object>>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.name())
                .detail("기획전 조회 성공")
                .data(
                    Map.of(
                        "totalPages",
                        specialExhibitionResponseDtoList.getTotalPages(),
                        "projects",
                        specialExhibitionResponseDtoList.getDataList()))
                .build());
  }

  @GetMapping("/makers/projects")
  public ResponseEntity<SuccessResponse<ProjectRegisteredByMakerResponseDto>>
      getProjectListRegisteredByMaker(
          @RequestHeader Long memberId,
          @PageableDefault(
                  page = 0,
                  size = 20,
                  sort = {"createdAt"},
                  direction = Sort.Direction.DESC)
              Pageable pageable) {

    return ResponseEntity.ok()
        .body(
            SuccessResponse.<ProjectRegisteredByMakerResponseDto>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .detail("등록된 프로젝트 조회")
                .message(HttpStatus.OK.name())
                .data(projectService.getRegisteredProject(memberId, pageable))
                .build());
  }

  @GetMapping("/makers/projects/{projectId}/status")
  public ResponseEntity<SuccessResponse<RegisteredProjectDetailForStatusResponseDto>>
      getRegisteredProjectDetailStatus(@PathVariable Long projectId) {
    return ResponseEntity.ok()
        .body(
            SuccessResponse.<RegisteredProjectDetailForStatusResponseDto>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .detail(HttpStatus.OK.name())
                .message("등록된 프로젝트 상세 조회")
                .data(projectService.getStatusOfRegisteredProject(projectId))
                .build());
  }

  @GetMapping("/makers/projects/{projectId}")
  public ResponseEntity<SuccessResponse<RegisteredProjectFundingListResponseDto>>
      getRegisteredProjectFundingListResponseDto(@PathVariable Long projectId) {
    RegisteredProjectFundingListResponseDto responseDto =
        projectService.getFundingListOfRegisteredProject(projectId);
    return ResponseEntity.ok()
        .body(
            SuccessResponse.<RegisteredProjectFundingListResponseDto>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.name())
                .detail("등록된 프로젝트 상세 조회")
                .data(responseDto)
                .build());
  }

  @GetMapping("/projects/{projectId}/support-signature/status")
  public ResponseEntity<SuccessResponse<Map<String, Boolean>>> getSupportSignatureStatus(
      @RequestHeader Long memberId, @PathVariable Long projectId) {

    Boolean isSupportSignature =
        projectForSupporterService.getIsSupportSignature(memberId, projectId);

    return ResponseEntity.ok()
        .body(
            SuccessResponse.<Map<String, Boolean>>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.name())
                .detail("해당 멤버의 프로젝트 지지서명 유무 조회")
                .data(Map.of("support-signature-status", isSupportSignature))
                .build());
  }

  @PostMapping("/presigned-url")
  public ResponseEntity<SuccessResponse<PresignedUrlResponseDto>> createPresignedUrl(
      @RequestBody ImageRequestDto imageRequestDto) {
    return ResponseEntity.ok()
        .body(
            SuccessResponse.<PresignedUrlResponseDto>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .data(
                    PresignedUrlResponseDto.builder()
                        .presignedUrl(
                            presignedUrlService.getPresignedUrl(
                                "project-img", imageRequestDto.getImageName()))
                        .build())
                .message("presigned url")
                .build());
  }
}
