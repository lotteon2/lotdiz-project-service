package com.lotdiz.projectservice.dto.request;

import com.lotdiz.projectservice.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class IsLikeOfMemberRequestDto {

  private Long memberId;
  private Long projectId;
}
