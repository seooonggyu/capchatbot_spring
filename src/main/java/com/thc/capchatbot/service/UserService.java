package com.thc.capchatbot.service;

import com.thc.capchatbot.dto.DefaultDto;
import com.thc.capchatbot.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    DefaultDto.CreateResDto create(UserDto.CreateReqDto param, Long reqUserId);

    void update(UserDto.UpdateReqDto param, Long reqUserId);

    void delete(UserDto.UpdateReqDto param, Long reqUserId);

    UserDto.DetailResDto detail(DefaultDto.DetailReqDto param, Long reqUserId);

    List<UserDto.DetailResDto> list(UserDto.ListReqDto param, Long reqUserId);
}
