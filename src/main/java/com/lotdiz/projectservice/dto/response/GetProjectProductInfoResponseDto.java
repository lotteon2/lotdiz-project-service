package com.lotdiz.projectservice.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class GetProjectProductInfoResponseDto {

  private Long projectId;
  private String projectDescription;
  private List<GetProductInfoDto> products;
}
