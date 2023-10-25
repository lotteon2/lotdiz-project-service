package com.lotdiz.projectservice.dto.response;

import java.util.List;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetTargetAmountCheckExceedResponseDto {

  private Long projectId;
  private String projectName;
  private Long makerMemberId;
  private Boolean isTargetAmountExceed;
  private List<Long> memberIds;
}
