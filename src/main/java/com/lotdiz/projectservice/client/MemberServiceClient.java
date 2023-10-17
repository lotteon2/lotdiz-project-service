package com.lotdiz.projectservice.client;

import com.lotdiz.projectservice.dto.response.MemberInfoResponseDto;
import com.lotdiz.projectservice.dto.response.MemberNameResponseDto;
import com.lotdiz.projectservice.utils.SuccessResponse;
import java.util.List;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

// @FeignClient(name = "memberServiceClient", url = "${endpoint.member-service}")
@FeignClient(
    name = "memberServiceClient",
    url = "https://e53d10c6-1cf7-4b4b-8ffb-47f7b1ed3862.mock.pstmn.io")
public interface MemberServiceClient {

  @GetMapping("/projects/islike")
  SuccessResponse<Map<String, Boolean>> getIsLike(
      @RequestHeader Long memberId, @RequestParam List<Long> projects);

  @GetMapping("/projects/{projectId}/like-count")
  SuccessResponse<Map<String, Long>> getLikeCount(@PathVariable Long projectId);

  @GetMapping("/members")
  SuccessResponse<Map<String, MemberInfoResponseDto>> getMemberInfo(
      @RequestParam List<Long> memberIds);

  @PostMapping("/members/name")
  SuccessResponse<MemberNameResponseDto> getMemberName(@RequestBody List<Long> memberId);
}
