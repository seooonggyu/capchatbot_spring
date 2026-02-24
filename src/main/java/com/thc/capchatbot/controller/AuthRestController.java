package com.thc.capchatbot.controller;

import com.thc.capchatbot.security.AuthService;
import com.thc.capchatbot.security.ExternalProperties;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthRestController {
    final AuthService authService;
    final ExternalProperties externalProperties;

    @PostMapping("") // 토큰 값 헤더에 담아서 주기
    public ResponseEntity<Void> access(HttpServletRequest request) {
        String refreshToken = request.getHeader("RefreshToken");

        String accessToken = null;
        if(refreshToken != null && refreshToken.startsWith(externalProperties.getTokenPrefix())) {
            String token = refreshToken.substring(externalProperties.getTokenPrefix().length());
            accessToken = authService.issueAccessToken(token);
        }

        return ResponseEntity.ok().header(externalProperties.getAccessKey(), externalProperties.getTokenPrefix() +  accessToken).build();
    }
}