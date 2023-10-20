package com.lotdiz.projectservice.exception;

import com.lotdiz.projectservice.exception.common.EntityNotFoundException;

public class MakerEntityNotfoundException extends EntityNotFoundException {
  private static final String message = "메이커 정보를 찾을 수 없습니다";

  public MakerEntityNotfoundException() {
    super(message);
  }
}
