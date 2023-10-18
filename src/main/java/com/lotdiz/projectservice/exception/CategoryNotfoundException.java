package com.lotdiz.projectservice.exception;

import com.lotdiz.projectservice.exception.common.EntityNotFoundException;

public class CategoryNotfoundException extends EntityNotFoundException {
  private static final String message = "카테고리를 찾을 수 없습니다";

  public CategoryNotfoundException() {
    super(message);
  }
}
