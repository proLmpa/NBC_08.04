package com.sparta.dtogram.follow.controller;

import com.sparta.dtogram.common.security.UserDetailsImpl;
import com.sparta.dtogram.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FollowController {

    private final FollowService followService;

    @PostMapping("/follow")
    public String follow(@AuthenticationPrincipal UserDetailsImpl followingUserDetails, @RequestParam Long followerId) {
        return followService.doFollow(followingUserDetails.getUser(),followerId);
    }

    @PostMapping("/unfollow")
    public String unFollow(@AuthenticationPrincipal UserDetailsImpl followingUserDetails, @RequestParam Long followerId) {
        return followService.unFollow(followingUserDetails.getUser(),followerId);
    }

}
