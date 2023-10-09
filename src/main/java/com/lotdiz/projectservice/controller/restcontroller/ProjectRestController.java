package com.lotdiz.projectservice.controller.restcontroller;

import com.lotdiz.projectservice.dto.response.ProjectByCategoryResponseDto;
import com.lotdiz.projectservice.dto.response.ProjectDetailResponseDto;
import com.lotdiz.projectservice.service.ProjectForSupporterService;
import com.lotdiz.projectservice.utils.SuccessResponse;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProjectRestController {

    private final ProjectForSupporterService projectForSupporterService;

    @GetMapping("/projects")
    public ResponseEntity<SuccessResponse> getProjectsByCategory(@RequestParam String category, @PageableDefault(page=0, size=20) Pageable pageable) {

        List<ProjectByCategoryResponseDto> projectByCategoryResponseDto = projectForSupporterService.getProjectsByCategory(category, pageable);

        return ResponseEntity.ok().body(
                SuccessResponse.builder()
                        .code(String.valueOf(HttpStatus.OK.value()))
                        .message(HttpStatus.OK.name())
                        .detail("카테고리 별, 프로젝트 조회 성공")
                        .data(Map.of("projects", projectByCategoryResponseDto)).build());
    }

    @GetMapping("/projects/{projectId}")
    public ResponseEntity<SuccessResponse> getProjectDetails(@PathVariable Long projectId) {

        ProjectDetailResponseDto projectDetailResponseDto = projectForSupporterService.getProjectDetails(projectId);

        return ResponseEntity.ok().body(
                SuccessResponse.builder()
                        .code(String.valueOf((HttpStatus.OK.value())))
                        .message(HttpStatus.OK.name())
                        .detail("프로젝트 상세 페이지 조회")
                        .data(Map.of("projectDetail", projectDetailResponseDto)).build());

    }


}
