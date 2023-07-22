package com.sparta.dtogram.admin.controller;

import com.sparta.dtogram.admin.service.AdminService;
import com.sparta.dtogram.common.dto.ApiResponseDto;
import com.sparta.dtogram.common.security.UserDetailsImpl;
import com.sparta.dtogram.profile.dto.ProfileRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminController {
    private final AdminService adminService;

    @PutMapping("/admin/user/{id}")
    public ResponseEntity<ApiResponseDto> editUser(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                     @PathVariable Long id,
                                                     @RequestBody ProfileRequestDto requestDto){
        try {
            adminService.editProfileByAdmin(userDetails.getUser(), id, requestDto);
            return ResponseEntity.ok().body(new ApiResponseDto("유저 정보 수정 성공", HttpStatus.OK.value()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }

    @PutMapping("/admin/user/{id}/{role}")
    public ResponseEntity<ApiResponseDto> editUser(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                   @PathVariable Long id,
                                                   @PathVariable String role){
        try {
            adminService.editRoleByAdmin(userDetails.getUser(), id, role);
            return ResponseEntity.ok().body(new ApiResponseDto("유저 정보 수정 성공", HttpStatus.OK.value()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }



    @DeleteMapping("/admin/user/{id}")
    public ResponseEntity<ApiResponseDto> deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                   @PathVariable Long id){
        try {
            adminService.deleteUserByAdmin(userDetails.getUser(), id);
            return ResponseEntity.ok().body(new ApiResponseDto("유저 삭제 완료", HttpStatus.OK.value()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }

}
