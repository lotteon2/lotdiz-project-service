package com.lotdiz.projectservice.dto.response;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PagedDataResponseDto<T> {

    private int totalPages;
    private T dataList;
}
