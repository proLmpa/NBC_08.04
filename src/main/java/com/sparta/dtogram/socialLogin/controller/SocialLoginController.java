package com.sparta.dtogram.socialLogin.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.dtogram.common.jwt.JwtUtil;
import com.sparta.dtogram.socialLogin.service.KakaoLoginService;
import com.sparta.dtogram.socialLogin.service.NaverLoginService;
import com.sparta.dtogram.user.service.UserService;
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
@RequestMapping("/api")
public class SocialLoginController {

    private final KakaoLoginService kakaoLoginService;
    private final NaverLoginService naverLoginService;

    @GetMapping("/user/kakao/callback") //버튼을 누르게 되면 카카오 서버로부터 리다이렉트되어 인가 코드를 전달받게됨. 해당 URL은 카카오 로그인 홈페이지에서 등록해뒀음.
    public String kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {

        String token = kakaoLoginService.kakaoLogin(code, response);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token); //check 토큰은 헤더에 넣어서 전달하는 방식.

        return "redirect:/";
    }

    @GetMapping("/user/naver/callback") //버튼을 누르게 되면 카카오 서버로부터 리다이렉트되어 인가 코드를 전달받게됨. 해당 URL은 카카오 로그인 홈페이지에서 등록해뒀음.
    public String naverLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {

        String token = naverLoginService.naverLogin(code, response);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token); //check 토큰은 헤더에 넣어서 전달하는 방식.

        return "redirect:/";
    }
}