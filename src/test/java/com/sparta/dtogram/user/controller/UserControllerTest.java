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
import org.springframework.test.web.servlet.MvcResult;

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
    private static String token;

    @Test
    @DisplayName("회원 가입 테스트")
    void signup() throws Exception {
        // given
        String username = "user2";
        String nickname = "2resu";
        String password = "user234@#$";
        String email = "2resu@email.com";

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
        String username = "user2";
        String password = "user234@#$";

        // when
        String body = mapper.writeValueAsString(
                LoginRequestDto.builder()
                        .username(username).password(password)
                        .build()
        );

        // then
        MvcResult result = mvc.perform(post(BASE_URL + "/user/login")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        token = result.getResponse().getHeader("Authorization");
    }

    @Test
    @DisplayName("단일 회원 정보 조회")
    void getUserInfo() throws Exception {
        // given

        // when

        // then
        mvc.perform(get(BASE_URL + "/user/info")
                    .header("Authorization", token)
                )
                .andExpect(status().isOk())
//                .andExpect(content().string("{\"id\":1,\"admin\":false}"))
                .andDo(print());
    }
}