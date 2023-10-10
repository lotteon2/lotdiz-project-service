package com.lotdiz.projectservice.exception;

public class FundingServiceClientOutOfServiceException extends RuntimeException {

    private static final String message = "펀딩 서비스가 정상적으로 작동중이지 않습니다.";

    public FundingServiceClientOutOfServiceException() {
        super(message);
    }
}