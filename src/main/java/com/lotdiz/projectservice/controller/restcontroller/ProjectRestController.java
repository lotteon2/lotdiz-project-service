package com.lotdiz.projectservice.controller.restcontroller;

import com.lotdiz.projectservice.dto.request.ProjectRegisterInformationRequestDto;
import com.lotdiz.projectservice.dto.request.SupportSignatureRequestDto;
import com.lotdiz.projectservice.dto.response.ProjectByCategoryResponseDto;
import com.lotdiz.projectservice.dto.response.ProjectDetailResponseDto;
import com.lotdiz.projectservice.dto.response.SupportSignatureResponseDto;
import com.lotdiz.projectservice.service.ProjectForSupporterService;
import com.lotdiz.projectservice.utils.SuccessResponse;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProjectRestController {

  private final ProjectForSupporterService projectForSupporterService;

  @GetMapping("/projects/category/{categoryName}")
  public ResponseEntity<SuccessResponse<Map<String, List<ProjectByCategoryResponseDto>>>>
      getProjectsByCategory(
          @RequestHeader Long memberId,
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

  @PostMapping("/project/makers/projects")
  public ResponseEntity<SuccessResponse<String>> registerProject(
      @RequestBody ProjectRegisterInformationRequestDto projectRegisterInformationRequestDto) {
    return ResponseEntity.ok().body(SuccessResponse.<String>builder().build());
  }
}
