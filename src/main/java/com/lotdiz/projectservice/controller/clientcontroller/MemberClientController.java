package com.lotdiz.projectservice.controller.clientcontroller;

import com.lotdiz.projectservice.dto.response.GetProjectInfoForLikesResponseDto;
import com.lotdiz.projectservice.service.MemberClientService;
import com.lotdiz.projectservice.utils.SuccessResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberClientController {

  private final MemberClientService memberClientService;

  @GetMapping ("/likes")
  private SuccessResponse<List<GetProjectInfoForLikesResponseDto>> getProjectInfoForLikes(
      @RequestParam List<Long> projectIds) {

    List<GetProjectInfoForLikesResponseDto> getProjectInfoForLikesResponseDtos =
        memberClientService.getProjectInfoForLikes(projectIds);

    return SuccessResponse.<List<GetProjectInfoForLikesResponseDto>>builder()
        .code(String.valueOf(HttpStatus.OK.value()))
        .message(HttpStatus.OK.name())
        .detail("project-service : 찜 목록 프로젝트 정보 요청 성공")
        .data(getProjectInfoForLikesResponseDtos)
        .build();
  }
}
