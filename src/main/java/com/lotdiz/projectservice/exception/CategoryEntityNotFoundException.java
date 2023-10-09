package com.lotdiz.projectservice.exception;

import com.lotdiz.projectservice.exception.common.EntityNotFoundException;

public class CategoryEntityNotFoundException extends EntityNotFoundException {

  private static final String message = "카테고리 정보를 찾을 수 없습니다.";

  public CategoryEntityNotFoundException() {
    super(message);
  }
}
