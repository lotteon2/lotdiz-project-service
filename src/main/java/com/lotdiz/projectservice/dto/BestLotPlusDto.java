package com.lotdiz.projectservice.dto;

import com.lotdiz.projectservice.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class BestLotPlusDto {

    private Project project;
    private Long supportSignatureCount;

}
