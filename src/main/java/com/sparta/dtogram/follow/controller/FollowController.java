package com.sparta.dtogram.follow.controller;

import com.sparta.dtogram.common.security.UserDetailsImpl;
import com.sparta.dtogram.follow.service.FollowService;
import com.sparta.dtogram.post.dto.PostRequestDto;
import com.sparta.dtogram.post.dto.PostResponseDto;
import com.sparta.dtogram.post.service.PostService;
import com.sparta.dtogram.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FollowController {

    private final FollowService followService;


    @GetMapping("/follow")
    public String follow(@RequestParam User followingUser, @AuthenticationPrincipal UserDetailsImpl followerUserDetails) {
        return followService.doFollow(followingUser, followerUserDetails.getUser());
    }

    @GetMapping("/unfollow")
    public String unFollow(@RequestParam User followingUser, @AuthenticationPrincipal UserDetailsImpl followerUserDetails) {
        return followService.unFollow(followingUser, followerUserDetails.getUser());
    }


}
