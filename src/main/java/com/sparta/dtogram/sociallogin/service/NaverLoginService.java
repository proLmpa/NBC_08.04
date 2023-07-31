package com.sparta.dtogram.sociallogin.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;

public interface NaverLoginService {
    /*
     * 네이버 로그인
     * @param code 네이버 로그인 후 생성된 코드 정보
     * @HttpServletResponse response 요청 결과를 응답 Header에 작성하기 위한 response 객체
     * @return token  네이버 로그인 후 생성된 토큰 정보
     */
    public String naverLogin(String code, HttpServletResponse response) throws JsonProcessingException;
}