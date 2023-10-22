package com.lotdiz.projectservice.dto.response;

import com.lotdiz.projectservice.dto.MemberNameDto;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberNameResponseDto {
  private List<MemberNameDto> memberNameDtos;
}
