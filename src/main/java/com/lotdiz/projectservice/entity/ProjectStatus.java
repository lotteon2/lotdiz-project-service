package com.lotdiz.projectservice.entity;

import lombok.Getter;

@Getter
public enum ProjectStatus {

    PENDING("프로젝트 준비중"),
    PROCESSING("프로젝트 진행중"),
    SUCCESS("프로젝트 성공"),
    FAIL("프로젝트 실패");

    private final String status;

    ProjectStatus(String status) {
        this.status = status;
    }
}
