package com.sparta.dtogram.follow.controller;

import com.sparta.dtogram.common.dto.ApiResponseDto;
import com.sparta.dtogram.common.security.UserDetailsImpl;
import com.sparta.dtogram.follow.service.FollowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "팔로우 API", description = "팔로우/언팔로우 기능을 위한 API 정보를 담고 있습니다.")
public class FollowController {

    private final FollowService followService;

    @Operation(summary = "사용자 팔로우", description = "전달된 Bearer 토큰을 통해 본인 확인 및 팔로우할 사용자의 ID를 통해 대상의 존재 유무 확인 후 둘 다 존재 시 새로운 팔로우를 생성합니다.")
    @PostMapping("/follow")
    public ResponseEntity<ApiResponseDto> followUser(@AuthenticationPrincipal UserDetailsImpl followerUserDetails, @RequestParam Long followingId) {
        try {
            String successMessage = followService.followUser(followerUserDetails.getUser().getId(), followingId);
            return ResponseEntity.ok(new ApiResponseDto(successMessage, HttpStatus.ACCEPTED.value()));
        } catch (IllegalAccessException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }

    @Operation(summary = "사용자 언팔로우", description = "전달된 Bearer 토큰을 통해 본인 확인 및 언팔로우할 사용자의 ID를 통해 대상의 존재 유무 확인 후 둘 다 존재 시 기존 팔로우를 삭제합니다.")
    @DeleteMapping("/follow")
    public ResponseEntity<ApiResponseDto> unfollowUser(@AuthenticationPrincipal UserDetailsImpl followerUserDetails, @RequestParam Long followingId) {
        String successMessage = followService.unfollowUser(followerUserDetails.getUser().getId(), followingId);
        return ResponseEntity.ok(new ApiResponseDto(successMessage, HttpStatus.ACCEPTED.value()));
    }
}