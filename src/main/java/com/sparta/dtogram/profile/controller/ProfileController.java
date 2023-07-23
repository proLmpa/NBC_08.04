package com.sparta.dtogram.profile.controller;

import com.sparta.dtogram.common.dto.ApiResponseDto;
import com.sparta.dtogram.common.dto.MsgResponseDto;
import com.sparta.dtogram.common.security.UserDetailsImpl;
import com.sparta.dtogram.profile.dto.PasswordRequestDto;
import com.sparta.dtogram.profile.dto.ProfileRequestDto;
import com.sparta.dtogram.profile.dto.ProfileResponseDto;
import com.sparta.dtogram.profile.service.ProfileService;
import com.sparta.dtogram.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.RejectedExecutionException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/profile/{id}")
    public ProfileResponseDto getProfile(@PathVariable Long id) {
        return profileService.getProfile(id);
    }

    @PutMapping("/profile")
    public ResponseEntity<MsgResponseDto> editProfile(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody ProfileRequestDto requestDto){
        try {
            profileService.editProfile(userDetails.getUser(), requestDto);
            return ResponseEntity.ok().body(new MsgResponseDto("프로필 수정 성공", HttpStatus.OK.value()));
        } catch (RejectedExecutionException e) {
            return ResponseEntity.badRequest().body(new MsgResponseDto("작성자만 수정 할 수 있습니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }

    @PutMapping("/profile/password")
    public ResponseEntity<MsgResponseDto> editPassword(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody PasswordRequestDto requestDto) {
        try {
            profileService.editPassword(userDetails.getUser(), requestDto);
            return ResponseEntity.ok().body(new MsgResponseDto("비밀번호 수정 성공", HttpStatus.OK.value()));
        } catch (RejectedExecutionException e) {
            return ResponseEntity.badRequest().body(new MsgResponseDto("작성자만 수정 할 수 있습니다.", HttpStatus.BAD_REQUEST.value()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MsgResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }

    @PutMapping("/profile/image")
    public ResponseEntity<ApiResponseDto> registerImage(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestPart("image") MultipartFile image) {
        try {
            profileService.updateImage(userDetails.getUser(), image);
            return ResponseEntity.ok().body(new ApiResponseDto("프로필 이미지 생성 성공", HttpStatus.OK.value()));
        }  catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }

    @DeleteMapping("/profile/image")
    public ResponseEntity<ApiResponseDto> deleteImage(@AuthenticationPrincipal UserDetailsImpl userDetails){
        profileService.deleteImage(userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto("프로필 이미지 삭제 성공", HttpStatus.OK.value()));
    }
}
