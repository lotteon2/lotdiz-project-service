package com.lotdiz.projectservice.mapper;

import com.lotdiz.projectservice.dto.request.CreateMakerRequestDto;
import com.lotdiz.projectservice.dto.request.MakerRegisterRequestDto;
import com.lotdiz.projectservice.entity.Maker;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MakerMapper {
  @Mapping(target = "memberId", ignore = true)
  @Mapping(target = "makerId", ignore = true)
  Maker makerRegisterRequestDtoToEntity(MakerRegisterRequestDto makerRegisterRequestDto);

  CreateMakerRequestDto makerEntityToCreateMakerRequestDto(Maker maker);
}
