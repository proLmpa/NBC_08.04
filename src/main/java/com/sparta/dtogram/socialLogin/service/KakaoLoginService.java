package com.sparta.dtogram.socialLogin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;

public interface KakaoLoginService {
    /*
     * 카카오 로그인
     * @param code 카카오 로그인 후 생성된 코드 정보
     * @HttpServletResponse response 요청 결과를 응답 Header에 작성하기 위한 response 객체
     * @return token  카카오 로그인 후 생성된 토큰 정보
     */
    public String kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException;
}
