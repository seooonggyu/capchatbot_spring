package com.thc.capchatbot.mapper;

import com.thc.capchatbot.dto.PermissiondetailDto;

import java.util.List;

public interface PermissiondetailMapper {
    PermissiondetailDto.DetailResDto detail(Long id);
    List<PermissiondetailDto.DetailResDto> list(PermissiondetailDto.ListReqDto param);
}