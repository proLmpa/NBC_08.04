package com.sparta.dtogram.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordRequestDto {
    private String password;
    private String newPassword1;
    private String newPassword2;
}
