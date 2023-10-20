package com.lotdiz.projectservice.exception;

import com.lotdiz.projectservice.exception.common.DomainException;
import org.apache.http.HttpStatus;

public class ProjectCannotUpdatableException extends DomainException {

  private static final String message = "프로젝트 정보를 수정할 수 없습니다.";

  public ProjectCannotUpdatableException() {
    super(message);
  }

  @Override
  public int getStatusCode() {
    return HttpStatus.SC_BAD_REQUEST;
  }
}
