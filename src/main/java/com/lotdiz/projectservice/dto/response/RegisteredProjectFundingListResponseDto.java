package com.lotdiz.projectservice.dto.response;

import com.lotdiz.projectservice.dto.RegisteredProjectFundingDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RegisteredProjectFundingListResponseDto {
  private List<RegisteredProjectFundingDto> registeredProjectFundingDtos;
}
