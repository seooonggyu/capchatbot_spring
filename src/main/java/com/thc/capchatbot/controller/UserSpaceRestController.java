package com.thc.capchatbot.controller;

import com.thc.capchatbot.dto.DefaultDto;
import com.thc.capchatbot.dto.UserSpaceDto;
import com.thc.capchatbot.security.PrincipalDetails;
import com.thc.capchatbot.service.UserSpaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/userSpace")
@RestController
public class UserSpaceRestController {
    final UserSpaceService userSpaceService;
    public Long getUserId(PrincipalDetails principalDetails) {
        if(principalDetails != null && principalDetails.getUser() != null) {
            return principalDetails.getUser().getId();
        }
        return null;
    }

    @PostMapping("/join")
    public ResponseEntity<Void> join(@RequestBody UserSpaceDto.JoinReqDto param, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        userSpaceService.join(param, getUserId(principalDetails));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/invite")
    public ResponseEntity<Void> invite(@RequestBody UserSpaceDto.InviteReqDto param, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        userSpaceService.invite(param, getUserId(principalDetails));
        return ResponseEntity.ok().build();
    }

    @PostMapping("")
    public ResponseEntity<DefaultDto.CreateResDto> create(@RequestBody UserSpaceDto.CreateReqDto param) {
        return ResponseEntity.ok(userSpaceService.create(param));
    }

    @PutMapping("")
    public ResponseEntity<Void> update(@RequestBody UserSpaceDto.UpdateReqDto param) {
        userSpaceService.update(param);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("")
    public ResponseEntity<Void> delete(@RequestBody UserSpaceDto.UpdateReqDto param) {
        userSpaceService.delete(param);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("")
    public ResponseEntity<UserSpaceDto.DetailResDto> detail(DefaultDto.DetailReqDto param) {
        return ResponseEntity.ok(userSpaceService.detail(param));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/list")
    public ResponseEntity<List<UserSpaceDto.DetailResDto>> list(UserSpaceDto.ListReqDto param, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ResponseEntity.ok(userSpaceService.list(param, getUserId(principalDetails)));
    }
}
