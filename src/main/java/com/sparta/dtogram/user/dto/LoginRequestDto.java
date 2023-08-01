package com.sparta.dtogram.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
public class LoginRequestDto {
    private String username;
    private String password;

    protected LoginRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}