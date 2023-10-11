package com.lotdiz.projectservice.exceptionhandler;

import com.lotdiz.projectservice.exception.common.DomainException;
import com.lotdiz.projectservice.utils.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ProjectRestControllerAdvice {

    private static final String UNIQUE_CONSTRAINT_EXCEPTION_MESSAGE = "유니크 제약조건 오류";
    private static final String DUPLICATE_KEY_EXCEPTION_MESSAGE = "중복 키 오류";
    private static final String DOMAIN_EXCEPTION_MESSAGE = "도메인 오류";

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> domainException(DomainException e) {
        int statusCode = e.getStatusCode();

        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(DOMAIN_EXCEPTION_MESSAGE)
                .detail(e.getMessage())
                .build();

        return ResponseEntity.status(statusCode).body(body);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> constraintViolationException(
            DataIntegrityViolationException e) {
        log.error("{}", e.getMessage());
        int statusCode = HttpStatus.BAD_REQUEST.value();
        ErrorResponse body =
                ErrorResponse.builder()
                        .code(String.valueOf(statusCode))
                        .message(UNIQUE_CONSTRAINT_EXCEPTION_MESSAGE)
                        .detail(e.getMessage())
                        .build();

        return ResponseEntity.status(statusCode).body(body);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ErrorResponse> duplicateKeyException(DuplicateKeyException e) {
        log.error("{}", e.getMessage());
        int statusCode = HttpStatus.BAD_REQUEST.value();
        ErrorResponse body =
                ErrorResponse.builder()
                        .code(String.valueOf(statusCode))
                        .message(DUPLICATE_KEY_EXCEPTION_MESSAGE)
                        .detail(e.getMessage())
                        .build();

        return ResponseEntity.status(statusCode).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> invalidRequestHandler(MethodArgumentNotValidException e) {
        log.error("{}", e.getMessage());
        int statusCode = HttpStatus.BAD_REQUEST.value();
        ErrorResponse errorResponse =
                ErrorResponse.builder()
                        .code(String.valueOf(statusCode))
                        .message(
                                e.getBindingResult().getFieldError() == null
                                        ? e.getMessage()
                                        : e.getBindingResult().getFieldError().getDefaultMessage())
                        .detail(e.getCause().getMessage())
                        .build();
        return ResponseEntity.status(statusCode).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(Exception e) {
        log.error("{}", e.getMessage());
        int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        ErrorResponse errorResponse =
                ErrorResponse.builder().code(String.valueOf(statusCode)).message(e.getMessage()).build();
        return ResponseEntity.status(statusCode).body(errorResponse);
    }




}
