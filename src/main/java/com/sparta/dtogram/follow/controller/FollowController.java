package com.sparta.dtogram.follow.controller;

import com.sparta.dtogram.common.dto.ApiResponseDto;
import com.sparta.dtogram.common.dto.MsgResponseDto;
import com.sparta.dtogram.common.security.UserDetailsImpl;
import com.sparta.dtogram.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponseDto> follow(@AuthenticationPrincipal UserDetailsImpl followingUserDetails, @RequestParam Long followerId) {
        try {
            followService.doFollow(followingUserDetails.getUser(), followerId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ApiResponseDto("팔로우 완료", HttpStatus.ACCEPTED.value()));
    }

    @PostMapping("/unfollow")
    public ResponseEntity<ApiResponseDto> unFollow(@AuthenticationPrincipal UserDetailsImpl followingUserDetails, @RequestParam Long followerId) {
        followService.unFollow(followingUserDetails.getUser(), followerId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ApiResponseDto("언팔로우 완료", HttpStatus.ACCEPTED.value()));
    }

}
