package com.lotdiz.projectservice.exception;

import com.lotdiz.projectservice.exception.common.EntityNotFoundException;

public class LotdealEntityNotFoundException extends EntityNotFoundException {

  private static final String message = "롯딜 프로젝트 정보를 찾을 수 없습니다.";

  public LotdealEntityNotFoundException() {
    super(message);
  }
}
