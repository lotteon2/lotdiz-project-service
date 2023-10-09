package com.lotdiz.projectservice.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ErrorResponse {

  private String code;
  private String message;
  private String detail;
}
