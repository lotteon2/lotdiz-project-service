package com.lotdiz.projectservice.client;

import com.lotdiz.projectservice.dto.response.FundingAchievementResultOfProjectDetailResponseDto;
import com.lotdiz.projectservice.dto.response.FundingAchievementResultOfProjectResponseDto;
import com.lotdiz.projectservice.utils.SuccessResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "fundingServiceClient", url = "https://952a1112-3483-413d-90a6-6befa9974329.mock.pstmn.io")
public interface FundingServiceClient {

  // TODO
  @GetMapping("/projects/achievement")
  SuccessResponse<HashMap<String, FundingAchievementResultOfProjectResponseDto>> getFundingOfProject(
      @RequestParam List<Long> projects);

  // TODO
  @GetMapping("/projects/{projectId}/achievement")
  SuccessResponse<FundingAchievementResultOfProjectDetailResponseDto> getFundingOfProjectDetail(
      @PathVariable Long projectId);
}
