package com.lotdiz.projectservice.exception;

import com.lotdiz.projectservice.exception.common.EntityNotFoundException;

public class SupportSignatureEntityNotFoundException extends EntityNotFoundException {

  private static final String message = "지지서명 정보를 찾을 수 없습니다.";

  public SupportSignatureEntityNotFoundException() {
    super(message);
  }
}
