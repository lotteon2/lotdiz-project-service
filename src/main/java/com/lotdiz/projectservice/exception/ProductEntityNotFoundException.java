package com.lotdiz.projectservice.exception;


import com.lotdiz.projectservice.exception.common.EntityNotFoundException;

public class ProductEntityNotFoundException extends EntityNotFoundException {

    private static final String message = "상품 정보를 찾을 수 없습니다.";

    public ProductEntityNotFoundException() {
        super(message);
    }
}
