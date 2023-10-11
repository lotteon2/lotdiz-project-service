package com.lotdiz.projectservice.client;

import com.lotdiz.projectservice.dto.request.IsLikeOfMemberRequestDto;
import com.lotdiz.projectservice.entity.Project;
import com.lotdiz.projectservice.utils.SuccessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "memberServiceClient", url = "${endpoint.member-service}")
public interface MemberServiceClient {

  @GetMapping("/projects/islike")
  SuccessResponse<Map<String, Boolean>> getIsLike(@RequestHeader Long memberId, @RequestParam List<Long> projects);

  @GetMapping("/projects/{projectId}/like-count")
  SuccessResponse<Map<String, Long>> getLikeCount(@PathVariable Long projectId);
}
