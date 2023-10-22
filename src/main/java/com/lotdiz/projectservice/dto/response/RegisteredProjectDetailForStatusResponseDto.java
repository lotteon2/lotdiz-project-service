package com.lotdiz.projectservice.dto.response;

import com.lotdiz.projectservice.dto.ProductInformationForRegisteredProjectDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RegisteredProjectDetailForStatusResponseDto {
  private final String projectName;
  private final LocalDateTime projectDueDate;
  private final Long remainDate;
  private final List<ProductInformationForRegisteredProjectDto> products;
  private Long fundingAchievementRate;
  private Long accumulatedFundingAmount;
  private Long numberOfBuyers;
}
