package com.thc.capchatbot.mapper;

import com.thc.capchatbot.dto.PermissionuserDto;

import java.util.List;

public interface PermissionuserMapper {
    PermissionuserDto.DetailResDto detail(Long id);
    List<PermissionuserDto.DetailResDto> list(PermissionuserDto.ListReqDto param);
}