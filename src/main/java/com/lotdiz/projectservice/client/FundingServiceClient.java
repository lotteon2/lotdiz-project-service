package com.lotdiz.projectservice.client;

import com.lotdiz.projectservice.dto.request.FundingAchievementResultOfProjectRequestDto;
import com.lotdiz.projectservice.dto.request.GetTargetAmountCheckExceedRequestDto;
import com.lotdiz.projectservice.dto.request.ProjectInformationForAchievedTargetAmountRequestDto;
import com.lotdiz.projectservice.dto.request.ProjectAmountWithIdRequestDto;
import com.lotdiz.projectservice.dto.request.ProjectInformationForAchievedTargetAmountRequestDto;
import com.lotdiz.projectservice.dto.response.FundingAchievementResultMapResponseDto;
import com.lotdiz.projectservice.dto.response.FundingAchievementResultOfProjectDetailResponseDto;
import com.lotdiz.projectservice.dto.response.FundingAchievementResultOfProjectResponseDto;
import com.lotdiz.projectservice.dto.response.GetTargetAmountCheckExceedResponseDto;
import com.lotdiz.projectservice.dto.response.MemberInformationOfFundingResponseDto;
import com.lotdiz.projectservice.dto.response.TargetAmountAchievedProjectsDto;
import com.lotdiz.projectservice.utils.SuccessResponse;
import java.util.HashMap;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "fundingServiceClient", url = "${endpoint.funding-service}")
public interface FundingServiceClient {

  @PostMapping("/projects/achievement")
  SuccessResponse<HashMap<String, FundingAchievementResultOfProjectResponseDto>>
      getFundingOfProject(@RequestBody List<FundingAchievementResultOfProjectRequestDto> projects);

  @GetMapping("/projects/{projectId}/achievement")
  SuccessResponse<FundingAchievementResultOfProjectDetailResponseDto> getFundingOfProjectDetail(
      @PathVariable Long projectId, @RequestParam Long projectTargetAmount);

  @PostMapping("/fundings/check-target-amount-exceed")
  SuccessResponse<List<GetTargetAmountCheckExceedResponseDto>> getTargetAmountCheckExceed(
      @RequestBody
          List<GetTargetAmountCheckExceedRequestDto> getTargetAmountCheckExceedRequestDtos);

  @PostMapping("/fundings/target-amount-projects-information")
  SuccessResponse<TargetAmountAchievedProjectsDto> getTargetAmountAchievedProjects(
      @RequestBody
          List<ProjectInformationForAchievedTargetAmountRequestDto>
              getTargetAmountCheckExceedRequestDtos);

  @GetMapping("/fundings/{projectId}/registered-project-detail")
  SuccessResponse<MemberInformationOfFundingResponseDto> getFundingList(
      @PathVariable Long projectId);

  @PostMapping("/fundings/registered-projects-check")
  SuccessResponse<FundingAchievementResultMapResponseDto> getRegisteredProject(
      @RequestBody ProjectAmountWithIdRequestDto projectAmountWithIdRequestDto);
}
