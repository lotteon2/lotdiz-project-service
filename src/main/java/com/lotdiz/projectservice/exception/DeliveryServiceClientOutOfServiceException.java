package com.lotdiz.projectservice.exception;

public class DeliveryServiceClientOutOfServiceException extends RuntimeException {
  private static final String message = "배송 서비스가 정상 동작하지 않습니다";

  public DeliveryServiceClientOutOfServiceException() {
    super(message);
  }
}
