package com.sparta.dtogram.common.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@Tag(name = "View API", description = "프론트엔드에서 html 파일 링크 요청 시 적합한 html 링크를 반환합니다.")
public class ViewController {
    @Operation(summary = "홈 화면으로 이동", description = "홈 화면으로 이동합니다.")
    @GetMapping("/")
    public String home() {
        return "index";
    }

    @Operation(summary = "로그인 화면으로 이동", description = "로그인 화면으로 이동합니다.")
    @GetMapping("/api/user/login-page")
    public String loginPage() {
        return "login";
    }

    @Operation(summary = "회원가입 화면으로 이동", description = "회원가입 화면으로 이동합니다.")
    @GetMapping("/api/user/signup")
    public String signupPage() {
        return "signup";
    }

    @Operation(summary = "마이페이지 화면으로 이동", description = "마이페이지 화면으로 이동합니다.")
    @GetMapping("/api/profile")
    public String getMyPage() {
        return "mypage";
    }
}
