package com.sparta.dtogram.socialLogin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NaverUserInfoDto {
    private String naverId;
    private String username;
    private String nickname;
    private String email;

    public NaverUserInfoDto(String naverId, String username, String nickname, String email) {
        this.naverId = naverId;
        this.username = username;
        this.nickname = nickname;
        this.email = email;
    }
}