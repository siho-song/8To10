package com.eighttoten.schedule;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.eighttoten.support.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("변동일정 컨트롤러 통합 테스트")
class VScheduleControllerTest {
    @Autowired
    TokenProvider tokenProvider;

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
    @DisplayName("변동일정 생성에 성공한다.")
    void add() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("title", "Test ScheduleEntity");
        request.put("commonDescription", "Test Description");
        request.put("startDateTime", LocalDateTime.of(2024, 9, 1, 10, 0).toString()); // LocalDateTime → String 변환
        request.put("endDateTime", LocalDateTime.of(2024, 9, 1, 12, 0).toString());

        String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/schedule/variable")
                        .header("Authorization","Bearer " + token) // JWT 쿠키 추가
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("변동일정 수정에 성공한다.")
    void update() throws Exception {
        //given
        Map<String, Object> request = new HashMap<>();
        request.put("id", 1L);
        request.put("title", "수정된 변동일정");
        request.put("commonDescription", "수정된 변동일정 입니다.");
        request.put("startDateTime", LocalDateTime.now().toString()); // LocalDateTime → String 변환
        request.put("endDateTime", LocalDateTime.now().plusHours(2L).toString());

        String body = objectMapper.writeValueAsString(request);

        //when,then
        mockMvc.perform(put("/schedule/variable")
                .header("Authorization","Bearer " + token)
                .contentType(APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("변동일정 단건 삭제에 성공한다.")
    void deleteById() throws Exception {
        //given
        Long id = 2L;

        //when,then
        mockMvc.perform(delete("/schedule/variable/{id}",id)
                .header("Authorization","Bearer " + token)
        ).andExpect(status().isNoContent());
    }
}