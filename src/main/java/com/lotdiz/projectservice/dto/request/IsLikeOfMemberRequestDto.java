package com.lotdiz.projectservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class IsLikeOfMemberRequestDto {

  private Long memberId;
  private List<Long> projects;
}
