package com.thc.capchatbot.mapper;

import com.thc.capchatbot.dto.GroupDto;

import java.util.List;

public interface GroupMapper {
    GroupDto.DetailResDto detail(Long id);
    List<GroupDto.DetailResDto> list(GroupDto.ListReqDto param);
}
