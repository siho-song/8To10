package com.eighttoten.presentation.schedule;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import com.eighttoten.schedule.dto.request.VScheduleSaveRequestRequest;
import com.eighttoten.schedule.dto.request.VScheduleUpdateRequest;
import com.eighttoten.infrastructure.TokenProvider;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("변동일정 엔드포인트 테스트")
@Transactional
class VScheduleControllerTest {

    @Autowired
    TokenProvider tokenProvider;  // TokenProvider 주입받기

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    String token;

    @BeforeEach
    void init(){
        token = tokenProvider.generateAccessToken("normal@example.com"); // 토큰 생성
    }

    @Test
    @DisplayName("변동일정 생성 엔드포인트 정상 작동")
    void add() throws Exception {

        VScheduleSaveRequestRequest dto = VScheduleSaveRequestRequest.builder()
                .title("Test Schedule")
                .commonDescription("Test Description")
                .start(LocalDateTime.of(2024, 9, 1, 10, 0))
                .end(LocalDateTime.of(2024, 9, 1, 12, 0))
                .build();

        mockMvc.perform(post("/schedule/variable")
                        .header("Authorization","Bearer " + token) // JWT 쿠키 추가
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("변동일정 수정 엔드포인트 정상 작동")
    void update() throws Exception {

        VScheduleUpdateRequest vScheduleUpdateRequest = new VScheduleUpdateRequest();
        vScheduleUpdateRequest.setId(8L);
        vScheduleUpdateRequest.setTitle("수정된 변동일정");
        vScheduleUpdateRequest.setCommonDescription("수정된 변동일정 입니다.");
        vScheduleUpdateRequest.setStartDate(LocalDateTime.now());
        vScheduleUpdateRequest.setEndDate(LocalDateTime.now().plusHours(2L));

        String dto = objectMapper.writeValueAsString(vScheduleUpdateRequest);

        mockMvc.perform(put("/schedule/variable")
                .header("Authorization","Bearer " + token)
                .contentType(APPLICATION_JSON)
                .content(dto)
        ).andExpect(status().isNoContent());
    }
}