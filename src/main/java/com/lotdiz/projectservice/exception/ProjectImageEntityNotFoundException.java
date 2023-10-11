package com.lotdiz.projectservice.exception;

import com.lotdiz.projectservice.exception.common.EntityNotFoundException;

public class ProjectImageEntityNotFoundException extends EntityNotFoundException {

  private static final String message = "프로젝트 상세 이미지 정보를 찾을 수 없습니다.";

  public ProjectImageEntityNotFoundException() {
    super(message);
  }
}
