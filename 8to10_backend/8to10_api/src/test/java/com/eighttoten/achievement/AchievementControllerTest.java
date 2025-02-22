package com.eighttoten.achievement;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.eighttoten.support.TokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("성취도 컨트롤러 테스트")
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
    @DisplayName("해당 멤버의 월간 성취도를 조회한다.")
    void month_achievement_search() throws Exception {
        //given
        int year = 2024;
        int month  = 1;

        //when
        ResultActions result = mockMvc.perform(get("/achievement/{year}/{month}", year, month)
                .header("Authorization", "Bearer " + token)
        );

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items.length()").value(10))
                .andExpect(jsonPath("$.items[0].achievementDate").value("2024-01-01"))
                .andExpect(jsonPath("$.items[0].achievementRate").value(90.0))
                .andExpect(jsonPath("$.items[9].achievementDate").value("2024-01-10"))
                .andExpect(jsonPath("$.items[9].achievementRate").value(90.0));
    }
}