package com.thc.capchatbot.mapper;

import com.thc.capchatbot.dto.UserDto;

import java.util.List;

public interface UserMapper {
    UserDto.DetailResDto detail(Long id);
    List<UserDto.DetailResDto> list(UserDto.ListReqDto param);
}
