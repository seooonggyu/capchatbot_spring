package com.thc.capchatbot.service;

import com.thc.capchatbot.dto.DefaultDto;
import com.thc.capchatbot.dto.PermissionuserDto;

import java.util.List;

public interface PermissionuserService {
    DefaultDto.CreateResDto create(PermissionuserDto.CreateReqDto param, Long reqUserId);
    void update(PermissionuserDto.UpdateReqDto param, Long reqUserId);
    void delete(PermissionuserDto.UpdateReqDto param, Long reqUserId);
    PermissionuserDto.DetailResDto detail(DefaultDto.DetailReqDto param, Long reqUserId);
    List<PermissionuserDto.DetailResDto> list(PermissionuserDto.ListReqDto param, Long reqUserId);
}
