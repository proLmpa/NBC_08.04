package com.sparta.dtogram.user.service;

import com.sparta.dtogram.user.dto.SignupRequestDto;
import com.sparta.dtogram.user.dto.UserInfoDto;
import com.sparta.dtogram.user.entity.User;

public interface UserService {
    /*
     * 회원 가입
     * @param requestDto 사용자 생성 요청 정보
    */
    void signup(SignupRequestDto requestDto);

    /*
     * 회원 정보 가져오기
     * @param user 사용자 권한 토큰
     * @return user 사용자 ID 및 ADMIN 여부 반환
    */
    UserInfoDto getUserInfo(User user);
}
