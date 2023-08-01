package com.sparta.dtogram.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SignupRequestDto {
    @NotBlank
    @Pattern(regexp = "[a-z0-9]{4,10}")
    private String username;
    @NotBlank
    private String nickname;
    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9!@#$%^&*]{8,15}")
    private String password;
    @Email
    @NotBlank
    private String email;
    private boolean admin;
    private String adminToken;
}
