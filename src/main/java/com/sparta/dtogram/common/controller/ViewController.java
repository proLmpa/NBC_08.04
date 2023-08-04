package com.sparta.dtogram.common.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class ViewController {
    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/api/user/login-page")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/api/user/signup")
    public String signupPage() {
        return "signup";
    }

    @GetMapping("/api/profile")
    public String getMyPage() {
        return "mypage";
    }
}
