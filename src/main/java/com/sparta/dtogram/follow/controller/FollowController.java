package com.sparta.dtogram.follow.controller;

import com.sparta.dtogram.common.dto.ApiResponseDto;
import com.sparta.dtogram.common.security.UserDetailsImpl;
import com.sparta.dtogram.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FollowController {

    private final FollowService followService;

    @PostMapping("/follow")
    public ResponseEntity<ApiResponseDto> followUser(@AuthenticationPrincipal UserDetailsImpl followerUserDetails, @RequestParam Long followingId) {
        try {
            String successMessage = followService.followUser(followerUserDetails.getUser().getId(), followingId);
            return ResponseEntity.ok(new ApiResponseDto(successMessage, HttpStatus.ACCEPTED.value()));
        } catch (IllegalAccessException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }

    @DeleteMapping("/follow")
    public ResponseEntity<ApiResponseDto> unfollowUser(@AuthenticationPrincipal UserDetailsImpl followerUserDetails, @RequestParam Long followingId) {
        String successMessage = followService.unfollowUser(followerUserDetails.getUser().getId(), followingId);
        return ResponseEntity.ok(new ApiResponseDto(successMessage, HttpStatus.ACCEPTED.value()));
    }
}