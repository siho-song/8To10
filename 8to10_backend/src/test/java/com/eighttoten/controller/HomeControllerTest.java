package com.eighttoten.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import com.eighttoten.provider.TokenProvider;

@AutoConfigureMockMvc
@SpringBootTest
@DisplayName("홈 컨트롤러 테스트")
class HomeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TokenProvider tokenProvider;

    String token;

    @BeforeEach
    void init(){
        token = tokenProvider.generateAccessToken("normal@example.com"); // 토큰 생성
    }

    @Test
    @DisplayName("유저 스텟 정보를 반환한다.")
    @Transactional
    void getUserStat() throws Exception {
        ResultActions result = mockMvc.perform(get("/home/user-stats")
                .header("Authorization", "Bearer " + token)
                .accept(APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").exists())
                .andExpect(jsonPath("$.nickname").isNotEmpty())
                .andExpect(jsonPath("$.role").exists())
                .andExpect(jsonPath("$.role").isNotEmpty())
                .andExpect(jsonPath("$.achievementRate").exists())
                .andExpect(jsonPath("$.achievementRate").isNotEmpty());
    }
}