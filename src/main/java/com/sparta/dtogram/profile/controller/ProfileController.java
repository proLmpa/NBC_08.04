package com.sparta.dtogram.profile.controller;

import com.sparta.dtogram.common.dto.ApiResponseDto;
import com.sparta.dtogram.common.security.UserDetailsImpl;
import com.sparta.dtogram.profile.dto.PasswordRequestDto;
import com.sparta.dtogram.profile.dto.ProfileRequestDto;
import com.sparta.dtogram.profile.dto.ProfileResponseDto;
import com.sparta.dtogram.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/profile/{id}")
    public ProfileResponseDto getProfile(@PathVariable Long id) {
        log.info("단일 프로필 조회");
        return profileService.getProfile(id);
    }

    @PutMapping("/profile")
    public ResponseEntity<ProfileResponseDto> editProfile(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody ProfileRequestDto requestDto){
        log.info("프로필 수정");
        ProfileResponseDto result = profileService.editProfile(userDetails.getUser(), requestDto);
        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/profile/password")
    public ResponseEntity<ApiResponseDto> editPassword(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody PasswordRequestDto requestDto) {
        log.info("비밀번호 수정");
        profileService.editPassword(userDetails.getUser(), requestDto);
        return ResponseEntity.ok().body(new ApiResponseDto("비밀번호 수정 성공", HttpStatus.OK.value()));

    }

    @PutMapping("/profile/image")
    public ResponseEntity<ApiResponseDto> updateImage(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestPart("image") MultipartFile image) {
        log.info("프로필 이미지 수정");
        profileService.updateImage(userDetails.getUser(), image);
        return ResponseEntity.ok().body(new ApiResponseDto("프로필 이미지 수정 성공", HttpStatus.OK.value()));
    }

    @DeleteMapping("/profile/image")
    public ResponseEntity<ApiResponseDto> deleteImage(@AuthenticationPrincipal UserDetailsImpl userDetails){
        log.info("프로필 이미지 삭제");
        profileService.deleteImage(userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto("프로필 이미지 삭제 성공", HttpStatus.OK.value()));
    }
}
