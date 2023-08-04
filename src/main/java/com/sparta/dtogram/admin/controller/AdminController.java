package com.sparta.dtogram.admin.controller;

import com.sparta.dtogram.admin.service.AdminService;
import com.sparta.dtogram.common.dto.ApiResponseDto;
import com.sparta.dtogram.common.security.UserDetailsImpl;
import com.sparta.dtogram.profile.dto.ProfileRequestDto;
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
@Tag(name = "관리자 API", description = "관리자의 사용자 정보 수정/권한 수정/정보 삭제를 위한 API 정보를 담고 있습니다.")
public class AdminController {
    private final AdminService adminService;

    @Operation(summary = "관리자의 사용자 정보 수정", description = "전달된 Bearer 토큰을 통해 본인 확인 및 사용자 ID를 통해 존재 여부 확인 후 둘 다 존재 시 ProfileRequestDto를 통해 기존 사용자의 프로필 정보를 수정합니다.")
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

    @Operation(summary = "관리자의 사용자 권한 수정", description = "전달된 Bearer 토큰을 통해 본인 확인 및 사용자 ID를 통해 존재 여부 확인 후 둘 다 존재 시 사용자의 권한을 role로 수정합니다.")
    @PutMapping("/admin/user/{id}/{role}")
    public ResponseEntity<ApiResponseDto> editUser(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                   @PathVariable Long id,
                                                   @PathVariable String role){
        try {
            adminService.editRoleByAdmin(userDetails.getUser(), id, role);
            return ResponseEntity.ok().body(new ApiResponseDto("유저 권한 수정 성공", HttpStatus.OK.value()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }

    @Operation(summary = "관리자의 사용자 정보 삭제", description = "전달된 Bearer 토큰을 통해 본인 확인 및 사용자 ID를 통해 존재 여부 확인 후 둘 다 존재 시 해당 사용자의 정보를 완전히 삭제합니다.")
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
