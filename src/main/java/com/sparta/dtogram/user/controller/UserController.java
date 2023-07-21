package com.sparta.dtogram.user.controller;


import com.sparta.dtogram.user.dto.ProfileResponseDto;
import com.sparta.dtogram.user.dto.SignupRequestDto;
import com.sparta.dtogram.user.entity.User;
import com.sparta.dtogram.user.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/login-page")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/user/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/user/signup")
    @ResponseBody
    public ResponseEntity<String> signup(@RequestBody @Valid SignupRequestDto requestDto, BindingResult bindingResult) {
        // Validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if(fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body("회원 가입 실패");
        }

        userService.signup(requestDto);

        return ResponseEntity.ok().body("회원 가입 성공");
    }


    @GetMapping("/user/{id}")
    public ResponseEntity<ProfileResponseDto> getUser(@PathVariable Long id){
        return ResponseEntity.ok().body(new ProfileResponseDto(userService.getUser(id)));
    }

    @GetMapping("/users")
    public ResponseEntity<List<ProfileResponseDto>> getAllUsers(){
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    @GetMapping("/users/{nickname}") //오버로딩
    public ResponseEntity<List<ProfileResponseDto>> getAllUsers(@PathVariable String nickname){
        return ResponseEntity.ok().body(userService.getAllUsers(nickname));
    }


}