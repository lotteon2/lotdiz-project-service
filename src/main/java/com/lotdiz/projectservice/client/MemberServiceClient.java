package com.lotdiz.projectservice.client;

import com.lotdiz.projectservice.dto.response.MemberInfoResponseDto;
import com.lotdiz.projectservice.dto.response.MemberLikesInfoResponseDto;
import com.lotdiz.projectservice.dto.response.MemberNameResponseDto;
import com.lotdiz.projectservice.utils.SuccessResponse;
import java.util.List;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "memberServiceClient", url = "${endpoint.member-service}")
public interface MemberServiceClient {

  @GetMapping("/projects/islike")
  SuccessResponse<Map<String, Boolean>> getIsLike(
      @RequestHeader Long memberId, @RequestParam List<Long> projects);

  @GetMapping("/projects/{projectId}/likes")
  SuccessResponse<MemberLikesInfoResponseDto> getLikes(@PathVariable Long projectId);

  @GetMapping("/projects/{projectId}/likes")
  SuccessResponse<MemberLikesInfoResponseDto> getLikesWithMemberId(
      @RequestHeader Long memberId, @PathVariable Long projectId);

  @GetMapping("/members")
  SuccessResponse<Map<String, MemberInfoResponseDto>> getMemberInfo(
      @RequestParam List<Long> memberIds);

  @PostMapping("/members/name")
  SuccessResponse<MemberNameResponseDto> getMemberName(@RequestBody List<Long> memberId);
}
