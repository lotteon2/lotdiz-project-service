package com.lotdiz.projectservice.client;

import com.lotdiz.projectservice.dto.request.IsLikeOfMemberRequestDto;
import com.lotdiz.projectservice.utils.SuccessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "member-service", url = "${endpoint.member-service}")
public interface MemberServiceClient {

  // TODO
  @GetMapping("/islike")
  SuccessResponse<Long> getIsLike(@RequestBody IsLikeOfMemberRequestDto isLikeOfMemberRequestDto);

  // TODO
  @GetMapping("/projects/{projectId}/like-count")
  SuccessResponse<Long> getCountLike(@PathVariable Long projectId);

}
