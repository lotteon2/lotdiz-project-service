package com.lotdiz.projectservice.dto.response;

import java.time.LocalDateTime;

import com.lotdiz.projectservice.entity.SupportSignature;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class SupportSignatureResponseDto {

  private Long supportSignatureId;
  private String supporterName;
  private String supporterImageUrl;
  private String supportSignatureContent;
  private LocalDateTime createdAt;
  private Boolean isSignatureOfLoggedUser;

  public static SupportSignatureResponseDto toDto(SupportSignature supportSignature, MemberInfoResponseDto memberInfoResponseDto, Long memberId) {

    return SupportSignatureResponseDto.builder()
            .supportSignatureId(supportSignature.getSupportSignatureId())
            .supporterName(memberInfoResponseDto.getMemberName())
            .supporterImageUrl(memberInfoResponseDto.getMemberProfileImageUrl())
            .supportSignatureContent(supportSignature.getSupportSignatureContent())
            .createdAt(supportSignature.getCreatedAt())
            .isSignatureOfLoggedUser(supportSignature.getMemberId() == memberId)
            .build();
  }
}
