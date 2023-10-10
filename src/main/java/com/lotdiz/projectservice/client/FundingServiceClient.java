package com.lotdiz.projectservice.client;

import com.lotdiz.projectservice.dto.request.GetTargetAmountCheckExceedRequestDto;
import com.lotdiz.projectservice.dto.request.ProjectInformationForAchievedTargetAmountRequestDto;
import com.lotdiz.projectservice.dto.response.GetTargetAmountCheckExceedResponseDto;
import com.lotdiz.projectservice.dto.response.TargetAmountAchievedProjectsDto;
import com.lotdiz.projectservice.utils.SuccessResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "fundingServiceClient", url = "${endpoint.funding-service}")
public interface FundingServiceClient {

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
