package com.lotdiz.projectservice.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MakerRegisterRequestDto {
  private final String makerEmail;
  private final String makerName;
  private final String makerImageUrl;
  private final String contactEmail;
  private final String makerPhoneNumber;
  @Builder.Default private final String makerKakaoUrl = null;
  @Builder.Default private final String makerHomeUrl = null;
  @Builder.Default private final String makerSnsUrl = null;
}
