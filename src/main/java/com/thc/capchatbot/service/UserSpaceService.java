package com.thc.capchatbot.service;

import com.thc.capchatbot.dto.DefaultDto;
import com.thc.capchatbot.dto.UserSpaceDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserSpaceService {
    void join(UserSpaceDto.JoinReqDto param, Long reqUserId);

    void invite(UserSpaceDto.InviteReqDto param, Long reqUserId);

    DefaultDto.CreateResDto create(UserSpaceDto.CreateReqDto param);

    void update(UserSpaceDto.UpdateReqDto param);

    void delete(UserSpaceDto.UpdateReqDto param);

    UserSpaceDto.DetailResDto detail(DefaultDto.DetailReqDto param);

    List<UserSpaceDto.DetailResDto> list(UserSpaceDto.ListReqDto param, Long reqUserId);
}
