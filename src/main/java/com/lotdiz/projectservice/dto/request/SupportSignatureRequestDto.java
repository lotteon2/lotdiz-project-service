package com.lotdiz.projectservice.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class SupportSignatureRequestDto {

  @Size(min = 5, max = 500, message = "내용은 5글자 이상 500 글자 이하만 입력 가능합니다.")
  @NotBlank(message = "내용은 null 이 불가합니다.")
  private String supportSignatureContents;
}
