package com.lotdiz.projectservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class MemberInfoResponseDto {

  private String memberName;
  private String memberProfileImageUrl;
}
