package com.lotdiz.projectservice.mapper;

import com.lotdiz.projectservice.dto.response.BannerResponseDto;
import com.lotdiz.projectservice.entity.Banner;
import java.util.List;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface BannerMapper {

  @Named("banner")
  BannerResponseDto toBannerDto(Banner banner);

  @IterableMapping(qualifiedByName = "banner")
  List<BannerResponseDto> toBannerDtoList(List<Banner> banners);
}
