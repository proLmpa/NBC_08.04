package com.sparta.dtogram.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.dtogram.user.dto.LoginRequestDto;
import com.sparta.dtogram.user.dto.SignupRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mvc;

    private static final String BASE_URL = "/api";

    @Test
    @DisplayName("회원 가입 테스트")
    void signup() throws Exception {
        // given
        String username = "test";
        String nickname = "testNick";
        String password = "testPassword";
        String email = "test@email.com";

        // when
        String body = mapper.writeValueAsString(
                SignupRequestDto.builder()
                        .username(username).nickname(nickname)
                        .password(password).email(email)
                        .admin(false).adminToken("")
                        .build()
        );

        // then
        mvc.perform(post(BASE_URL + "/user/signup")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    @DisplayName("로그인")
    void login() throws Exception {
        // given
        String username = "test";
        String password = "testPassword";

        // when
        String body = mapper.writeValueAsString(
                LoginRequestDto.builder()
                        .username(username).password(password)
                        .build()
        );

        // then
        mvc.perform(post(BASE_URL + "/user/login")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("단일 회원 정보 조회")
    void getUserInfo() throws Exception {
        // given
        String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiYXV0aCI6IlVTRVIiLCJleHAiOjE2OTA4OTI1NjgsImlhdCI6MTY5MDg1NjU2OH0.uePF9-o0NmEEAX6xkBrw1cpKfUU2WLELeVP7a84V42c";

        // when

        // then
        mvc.perform(get(BASE_URL + "/user/info")
                    .header("Authorization", token)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":1,\"admin\":false}"))
                .andDo(print());
    }
}