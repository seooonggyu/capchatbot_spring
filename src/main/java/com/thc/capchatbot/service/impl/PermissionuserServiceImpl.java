package com.thc.capchatbot.service.impl;

import com.thc.capchatbot.domain.Permissionuser;
import com.thc.capchatbot.domain.User;
import com.thc.capchatbot.dto.DefaultDto;
import com.thc.capchatbot.dto.PermissionuserDto;
import com.thc.capchatbot.exception.NoMatchingDataException;
import com.thc.capchatbot.mapper.PermissionuserMapper;
import com.thc.capchatbot.repository.PermissionuserRepository;
import com.thc.capchatbot.repository.UserRepository;
import com.thc.capchatbot.service.PermissionuserService;
import com.thc.capchatbot.service.PermittedService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PermissionuserServiceImpl implements PermissionuserService {

    final PermissionuserRepository permissionuserRepository;
    final PermissionuserMapper permissionuserMapper;
    final UserRepository userRepository;
    final PermittedService permittedService;

    String target = "permission";

    @Override
    public DefaultDto.CreateResDto create(PermissionuserDto.CreateReqDto param, Long reqUserId) {
        if (reqUserId != null) {
            permittedService.check(target, 110, reqUserId);
        }

        if(param.getUserId() == null){
            User user = userRepository.findByUsername(param.getUserUsername());
            if(user == null){
                throw new NoMatchingDataException("no matching data");
            } else {
                param.setUserId(user.getId());
            }
        }

        Permissionuser permissionuser = permissionuserRepository.findByPermissionIdAndUserId(param.getPermissionId(), param.getUserId());
        if (permissionuser != null) {
            return permissionuser.toCreateResDto();
        }

        DefaultDto.CreateResDto res = permissionuserRepository.save(param.toEntity()).toCreateResDto();

        return res;
    }

    @Override
    public void update(PermissionuserDto.UpdateReqDto param, Long reqUserId) {
        permittedService.check(target, 120, reqUserId);

        Permissionuser permissionuser = permissionuserRepository.findById(param.getId()).orElseThrow(() -> new RuntimeException("no data"));

        permissionuser.update(param);
        permissionuserRepository.save(permissionuser);
    }

    @Override
    public void delete(PermissionuserDto.UpdateReqDto param, Long reqUserId) {
        update(PermissionuserDto.UpdateReqDto.builder().id(param.getId()).deleted(true).build(), reqUserId);
    }

    public PermissionuserDto.DetailResDto get(DefaultDto.DetailReqDto param, Long reqUserId) {
        permittedService.check(target, 200, reqUserId);

        PermissionuserDto.DetailResDto res = permissionuserMapper.detail(param.getId());

        return res;
    }

    @Override
    public PermissionuserDto.DetailResDto detail(DefaultDto.DetailReqDto param, Long reqUserId) {
        return get(param, reqUserId);
    }

    public List<PermissionuserDto.DetailResDto> addlist(List<PermissionuserDto.DetailResDto> list, Long reqUserId) {
        List<PermissionuserDto.DetailResDto> newList = new ArrayList<>();
        for (PermissionuserDto.DetailResDto permissionuser : list) {
            newList.add(get(DefaultDto.DetailReqDto.builder().id(permissionuser.getId()).build(), reqUserId));
        }
        return newList;
    }

    @Override
    public List<PermissionuserDto.DetailResDto> list(PermissionuserDto.ListReqDto param, Long reqUserId) {
        List<PermissionuserDto.DetailResDto> list = new ArrayList<>();
        List<PermissionuserDto.DetailResDto> permissionusers = permissionuserMapper.list(param);
        return addlist(permissionusers, reqUserId);
    }
}
