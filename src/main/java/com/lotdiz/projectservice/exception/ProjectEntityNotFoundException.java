package com.lotdiz.projectservice.exception;

import com.lotdiz.projectservice.exception.common.EntityNotFoundException;

public class ProjectEntityNotFoundException extends EntityNotFoundException {

  private static final String message = "프로젝트 정보를 찾을 수 없습니다.";

  public ProjectEntityNotFoundException() {
    super(message);
  }
}
