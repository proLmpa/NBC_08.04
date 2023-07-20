package com.sparta.dtogram.common.controller;

import com.sparta.dtogram.common.security.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class HomeController {
    @GetMapping("/")
    //public String home(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    public String home() {
        //String name = userDetails.getUsername();
        //log.info("###" + name);
        return "index";
    }
}
