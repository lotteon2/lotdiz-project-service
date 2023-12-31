package com.lotdiz.projectservice.dto.request;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthorizedProjectRequestDto {

  private Long projectId;
}
