package com.lotdiz.projectservice.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lotdiz.projectservice.dto.response.FundingAchievementResultOfProjectResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;

@Getter
@Builder
@AllArgsConstructor
public class SuccessResponse<T> {

  private String code;
  private String message;
  private String detail;

  @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
  private T data;
}
