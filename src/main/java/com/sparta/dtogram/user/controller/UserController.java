package com.sparta.dtogram.user.controller;

import com.sparta.dtogram.common.dto.ApiResponseDto;
import com.sparta.dtogram.common.error.DtogramErrorCode;
import com.sparta.dtogram.common.exception.DtogramException;
import com.sparta.dtogram.common.security.UserDetailsImpl;
import com.sparta.dtogram.user.dto.SignupRequestDto;
import com.sparta.dtogram.user.dto.UserInfoDto;
import com.sparta.dtogram.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@Tag(name = "사용자 API", description = "사용자의 회원 가입, 로그인 관련 기능 및 단일 회원 조회를 위한 API 정보를 담고 있습니다.")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "회원 가입", description = "SignupRequesetDto를 통해 회원이 제출한 정보의 유효성 검사 후 통과 시 DB에 저장하고 성공 메시지를 반환합니다.")
    @PostMapping("/user/signup")
    public ResponseEntity<ApiResponseDto> signup(@RequestBody @Valid SignupRequestDto requestDto, BindingResult bindingResult) {
        log.info("회원 가입");

        // Validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if(!fieldErrors.isEmpty()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            throw new DtogramException(DtogramErrorCode.INVALID_TYPE_VALUE, null);
        }

        userService.signup(requestDto);
        return ResponseEntity.ok().body(new ApiResponseDto("회원 가입 성공", 200));
    }

    @Operation(summary = "단일 회원 정보 조회", description = "전달된 Bearer 토큰을 통해 본인 확인을 마친 후 자신의 ID와 권한을 반환한다.")
    @GetMapping("/user/info")
    public ResponseEntity<UserInfoDto> getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        log.info("단일 회원 정보 조회");
        return ResponseEntity.ok(userService.getUserInfo(userDetailsImpl.getUser()));
    }
}