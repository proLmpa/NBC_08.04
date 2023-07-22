package com.sparta.dtogram.profile.dto;

import com.sparta.dtogram.user.entity.User;
import com.sparta.dtogram.user.entity.UserRoleEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileResponseDto {
    private String username;
    private String nickname;
    private String email;
    private String introduction;
    private UserRoleEnum role;
    private String imageUrl;

    public ProfileResponseDto(User user) {
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.introduction = user.getIntroduction();
        this.role = user.getRole();
        this.imageUrl = user.getImageUrl();
    }
}
