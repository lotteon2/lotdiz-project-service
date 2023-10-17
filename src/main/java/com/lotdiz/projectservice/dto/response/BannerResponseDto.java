package com.lotdiz.projectservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class BannerResponseDto {

  private Long bannerId;
  private String bannerImageUrl;
  private String bannerUrl;
}
