package com.thc.capchatbot.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thc.capchatbot.dto.UserDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Transactional
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final AuthService authService;
    private final ExternalProperties externalProperties;

    /**
     *  로그인하려는 사용자의 자격을 확인해 토큰을 발급하는 함수.
     *  "/api/login" 으로 들어오는 요청에 실행된다.
     *  생성된 Authentication이 SecurityContextHolder에 등록되어 권한처리가 가능하게 한다.
     *
     *  @throws AuthenticationException
     */
    @Transactional
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        Authentication authentication = null;
        UserDto.LoginReqDto userLoginDto = null;

        try {
            userLoginDto = objectMapper.readValue(request.getInputStream(), UserDto.LoginReqDto.class);
        } catch (IOException e) {
            // 요청 데이터 파싱 에러는 RuntimeException 등으로 던져서 처리하거나 로그 남김
            throw new RuntimeException("Login Request Parsing Error", e);
        }

        // 아이디/비번 토큰 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userLoginDto.getUsername(), userLoginDto.getPassword());

        // authenticate 메서드는 실패 시 AuthenticationException을 던집니다.
        // 이를 try-catch로 잡지 않아야 Spring Security가 자동으로 unsuccessfulAuthentication을 호출합니다.
        authentication = authenticationManager.authenticate(authenticationToken);

        return authentication;
    }

    /**
     *  로그인 완료시 호출되는 함수.
     *  Refresh Token을 발급해 HttpServletRespons에 담는다.
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        // UserId로 리프레시 토큰 발급
        String refreshToken = authService.createRefreshToken(principalDetails.getUser().getId());

        // Header에 담아서 전달
        response.addHeader(externalProperties.getRefreshKey(), externalProperties.getTokenPrefix() + refreshToken);

        System.out.println("Login Success");
    }
}
