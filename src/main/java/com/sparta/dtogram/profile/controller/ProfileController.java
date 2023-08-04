package com.sparta.dtogram.profile.controller;

import com.sparta.dtogram.common.dto.ApiResponseDto;
import com.sparta.dtogram.common.security.UserDetailsImpl;
import com.sparta.dtogram.profile.dto.PasswordRequestDto;
import com.sparta.dtogram.profile.dto.ProfileRequestDto;
import com.sparta.dtogram.profile.dto.ProfileResponseDto;
import com.sparta.dtogram.profile.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "프로필 API", description = "프로필 조회/수정/삭제 기능 및 프로필 이미지 수정/해제를 위한 API 정보를 담고 있습니다.")
public class ProfileController {

    private final ProfileService profileService;

    @Operation(summary = "단일 프로필 조회", description = "사용자 ID를 통해 사용자 정보 확인 후 존재 시 해당 사용자의 프로필을 반환합니다.")
    @GetMapping("/profile/{id}")
    public ProfileResponseDto getProfile(@PathVariable Long id) {
        log.info("단일 프로필 조회");
        return profileService.getProfile(id);
    }

    @Operation(summary = "프로필 수정", description = "전달된 Bearer 토큰을 통해 본인 확인을 마친 후 ProfileRequestDto를 통해 사용자 프로필 내용을 수정합니다.")
    @PutMapping("/profile")
    public ResponseEntity<ProfileResponseDto> editProfile(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody ProfileRequestDto requestDto){
        log.info("프로필 수정");
        ProfileResponseDto result = profileService.editProfile(userDetails.getUser(), requestDto);
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "비밀번호 수정", description = "전달된 Bearer 토큰을 통해 본인 확인을 마친 후 PasswordRequestDto를 통해 사용자 비밀번호를 수정합니다.")
    @PatchMapping("/profile/password")
    public ResponseEntity<ApiResponseDto> editPassword(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody PasswordRequestDto requestDto) {
        log.info("비밀번호 수정");
        profileService.editPassword(userDetails.getUser(), requestDto);
        return ResponseEntity.ok().body(new ApiResponseDto("비밀번호 수정 성공", HttpStatus.OK.value()));

    }


    @Operation(summary = "프로필 이미지 수정", description = "전달된 Bearer 토큰을 통해 본인 확인을 마친 후 전달된 image로 사용자 프로필 이미지를 수정합니다.")
    @PutMapping("/profile/image")
    public ResponseEntity<ApiResponseDto> updateImage(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestPart("image") MultipartFile image) {
        log.info("프로필 이미지 수정");
        profileService.updateImage(userDetails.getUser(), image);
        return ResponseEntity.ok().body(new ApiResponseDto("프로필 이미지 수정 성공", HttpStatus.OK.value()));
    }

    @Operation(summary = "프로필 이미지 수정", description = "전달된 Bearer 토큰을 통해 본인 확인을 마친 후 기존 프로필 이미지를 삭제합니다.")
    @DeleteMapping("/profile/image")
    public ResponseEntity<ApiResponseDto> deleteImage(@AuthenticationPrincipal UserDetailsImpl userDetails){
        log.info("프로필 이미지 삭제");
        profileService.deleteImage(userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto("프로필 이미지 삭제 성공", HttpStatus.OK.value()));
    }
}
