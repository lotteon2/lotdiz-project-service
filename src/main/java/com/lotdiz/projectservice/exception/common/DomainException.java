package com.lotdiz.projectservice.exception.common;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class DomainException extends RuntimeException {
    private final Map<String, String> validation = new HashMap<>();

    public DomainException(String message) {
        super(message);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String errorMessage) {
        validation.put(fieldName, errorMessage);
    }
}
