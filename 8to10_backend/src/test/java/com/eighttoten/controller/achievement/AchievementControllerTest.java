package com.eighttoten.controller.achievement;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import com.eighttoten.provider.TokenProvider;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("성취도 컨트롤러 테스트")
@Transactional
class AchievementControllerTest {

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
    @DisplayName("유저의 월간 성취도 조회")
    void month_achievement_search() throws Exception {
        int year = 2024;
        int month  = 1;
        mockMvc.perform(get("/achievement/{year}/{month}", year, month)
                        .header("Authorization","Bearer " + token)
                ).andExpect(status().isOk());
    }
}