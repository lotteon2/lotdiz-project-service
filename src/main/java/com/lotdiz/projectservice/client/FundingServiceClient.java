package com.lotdiz.projectservice.client;

import com.lotdiz.projectservice.dto.response.FundingAchievementResultOfProjectDetailResponseDto;
import com.lotdiz.projectservice.dto.response.FundingAchievementResultOfProjectResponseDto;
import com.lotdiz.projectservice.utils.SuccessResponse;
import com.lotdiz.projectservice.dto.request.GetTargetAmountCheckExceedRequestDto;
import com.lotdiz.projectservice.dto.request.ProjectInformationForAchievedTargetAmountRequestDto;
import com.lotdiz.projectservice.dto.response.GetTargetAmountCheckExceedResponseDto;
import com.lotdiz.projectservice.dto.response.TargetAmountAchievedProjectsDto;

import java.util.HashMap;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "fundingServiceClient", url = "https://e53d10c6-1cf7-4b4b-8ffb-47f7b1ed3862.mock.pstmn.io")
public interface FundingServiceClient {

  @GetMapping("/projects/achievement")
  SuccessResponse<HashMap<String, FundingAchievementResultOfProjectResponseDto>>
      getFundingOfProject(@RequestParam List<Long> projects);

  @GetMapping("/projects/{projectId}/achievement")
  SuccessResponse<FundingAchievementResultOfProjectDetailResponseDto> getFundingOfProjectDetail(
      @PathVariable Long projectId);

  @PostMapping("/fundings/check-target-amount-exceed")
  SuccessResponse<List<GetTargetAmountCheckExceedResponseDto>> getTargetAmountCheckExceed(
      @RequestBody
          List<GetTargetAmountCheckExceedRequestDto> getTargetAmountCheckExceedRequestDtos);

  @PostMapping("/fundings/target-amount-projects-information")
  SuccessResponse<TargetAmountAchievedProjectsDto> getTargetAmountAchievedProjects(
      @RequestBody
          List<ProjectInformationForAchievedTargetAmountRequestDto>
              getTargetAmountCheckExceedRequestDtos);
}
