package com.thc.capchatbot.mapper;

import com.thc.capchatbot.dto.SpaceDto;

import java.util.List;

public interface SpaceMapper {
    SpaceDto.DetailResDto detail(Long id);

    List<SpaceDto.DetailResDto> list(SpaceDto.ListReqDto param);
}
