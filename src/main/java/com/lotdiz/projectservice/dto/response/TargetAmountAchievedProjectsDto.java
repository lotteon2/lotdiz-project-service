package com.lotdiz.projectservice.dto.response;

import com.lotdiz.projectservice.dto.TargetAmountAchievedDto;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TargetAmountAchievedProjectsDto {

  private List<TargetAmountAchievedDto> targetAmountAchievedDtos;
}
