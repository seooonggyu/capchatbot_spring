package com.thc.capchatbot.mapper;

import com.thc.capchatbot.dto.FileDto;

import java.util.List;

public interface FileMapper {
    List<FileDto.DetailResDto> listItems(FileDto.ListReqDto param);
}
