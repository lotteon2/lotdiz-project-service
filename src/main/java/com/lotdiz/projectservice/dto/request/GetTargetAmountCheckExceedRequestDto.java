package com.lotdiz.projectservice.dto.request;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetTargetAmountCheckExceedRequestDto {

  private Long projectId;
  private String projectName;
  private Long makerMemberId;
  private Long projectTargetAmount;
}
