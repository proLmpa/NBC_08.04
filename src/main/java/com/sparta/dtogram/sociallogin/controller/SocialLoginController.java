package com.sparta.dtogram.sociallogin.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.dtogram.common.jwt.JwtUtil;
import com.sparta.dtogram.sociallogin.service.KakaoLoginService;
import com.sparta.dtogram.sociallogin.service.NaverLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
@Tag(name = "소셜 로그인 API", description = "카카오 로그인과 네이버 로그인을 API 정보를 담고 있습니다.")
@RequestMapping("/api")
public class SocialLoginController {

    private final KakaoLoginService kakaoLoginService;
    private final NaverLoginService naverLoginService;

    @Operation(summary = "카카오 로그인", description = "프론트엔드에서 버튼을 누를 시 카카오 서버로부터 리다이렉트되어 전달된 인가코드를 통해 카카오 로그인을 진행합니다.")
    @GetMapping("/user/kakao/callback")
    public String kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {

        String token = kakaoLoginService.kakaoLogin(code, response);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token); //check 토큰은 헤더에 넣어서 전달하는 방식.

        return "redirect:/";
    }

    @Operation(summary = "네이버 로그인", description = "프론트엔드에서 버튼을 누를 시 네이버 서버로부터 리다이렉트되어 전달된 인가코드를 통해 네이버 로그인을 진행합니다.")
    @GetMapping("/user/naver/callback")
    public String naverLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {

        String token = naverLoginService.naverLogin(code, response);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token); //check 토큰은 헤더에 넣어서 전달하는 방식.

        return "redirect:/";
    }
}