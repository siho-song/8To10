package com.eighttoten.schedule;

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
@DisplayName("일정 공통 컨트롤러 통합 테스트")
class ScheduleAbleControllerTest {
    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    MockMvc mockMvc;

    String token ;

    @BeforeEach
    void init(){
        token = tokenProvider.generateAccessToken("normal2@example.com"); // 토큰 생성
    }

    @Test
    @DisplayName("해당 멤버의 모든일정을 조회한다.")
    void getAllSchedule() throws Exception {
        //when
        ResultActions result = mockMvc.perform(get("/schedule")
                .header("Authorization", "Bearer " + token));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items.length()").value(12))

                .andExpect(jsonPath("$.items[0].id").value(3))
                .andExpect(jsonPath("$.items[0].title").value("variable Schedule3"))
                .andExpect(jsonPath("$.items[0].type").value("variable"))
                .andExpect(jsonPath("$.items[0].startDateTime").value("2024-10-01T00:00:00"))
                .andExpect(jsonPath("$.items[0].endDateTime").value("2024-10-01T00:00:00"))
                .andExpect(jsonPath("$.items[0].color").value("#f4511e"))

                .andExpect(jsonPath("$.items[1].id").value(7))
                .andExpect(jsonPath("$.items[1].title").value("fixed schedule2"))
                .andExpect(jsonPath("$.items[1].type").value("fixed"))
                .andExpect(jsonPath("$.items[1].startDateTime").value("2024-02-01T10:00:00"))
                .andExpect(jsonPath("$.items[1].endDateTime").value("2024-02-01T14:00:00"))
                .andExpect(jsonPath("$.items[1].color").value("#3788d8"))
                .andExpect(jsonPath("$.items[1].parentId").value(2))
                .andExpect(jsonPath("$.items[1].detailDescription").value("Detail of the second F_SCHEDULE."))

                .andExpect(jsonPath("$.items[6].id").value(11))
                .andExpect(jsonPath("$.items[6].title").value("normal schedule2"))
                .andExpect(jsonPath("$.items[6].type").value("normal"))
                .andExpect(jsonPath("$.items[6].startDateTime").value("2024-06-01T09:00:00"))
                .andExpect(jsonPath("$.items[6].endDateTime").value("2024-06-01T10:00:00"))
                .andExpect(jsonPath("$.items[6].color").value("#4CAF50"))
                .andExpect(jsonPath("$.items[6].parentId").value(3))
                .andExpect(jsonPath("$.items[6].detailDescription").value("Detail of the second N_SCHEDULE."))
                .andExpect(jsonPath("$.items[6].dailyAmount").value(40))
                .andExpect(jsonPath("$.items[6].achievedAmount").value(0))
                .andExpect(jsonPath("$.items[6].bufferTime").value("01:00:00"))
                .andExpect(jsonPath("$.items[6].completeStatus").value(false));
    }
}