package com.lotdiz.projectservice.client;

import com.lotdiz.projectservice.dto.response.FundingAchievementResultOfProjectDetailResponseDto;
import com.lotdiz.projectservice.dto.response.FundingAchievementResultOfProjectResponseDto;
import com.lotdiz.projectservice.utils.SuccessResponse;
import java.util.HashMap;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "fundingServiceClient", url = "${endpoint.funding-service}")
public interface FundingServiceClient {

  @GetMapping("/projects/achievement")
  SuccessResponse<HashMap<String, FundingAchievementResultOfProjectResponseDto>>
      getFundingOfProject(@RequestParam List<Long> projects);

  @GetMapping("/projects/{projectId}/achievement")
  SuccessResponse<FundingAchievementResultOfProjectDetailResponseDto> getFundingOfProjectDetail(
      @PathVariable Long projectId);
}
