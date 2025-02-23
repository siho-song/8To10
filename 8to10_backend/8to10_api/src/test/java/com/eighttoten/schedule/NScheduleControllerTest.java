package com.eighttoten.schedule;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.eighttoten.support.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
@DisplayName("일반일정 컨트롤러 통합테스트")
public class NScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    TokenProvider tokenProvider;

    String token;

    @BeforeEach
    public void init() {
        token = tokenProvider.generateAccessToken("normal@example.com"); // 토큰 생성
    }

    @Test
    @DisplayName("일반일정과 자식일정을 함께 등록한다.")
    public void add() throws Exception {
        //given
        Map<String, Object> request = new HashMap<>();
        request.put("title", "Test ScheduleEntity");
        request.put("commonDescription", "Test Description");
        request.put("startDateTime", LocalDateTime.of(2024, 1, 1,0,0).toString());
        request.put("endDateTime", LocalDateTime.of(2024, 4, 1,0,0).toString());
        request.put("bufferTime", LocalTime.of(1, 0).toString());
        request.put("performInDay", LocalTime.of(2, 0).toString());
        request.put("isIncludeSaturday", true);
        request.put("isIncludeSunday", false);
        request.put("totalAmount", 10);
        request.put("performInWeek", 3);
        String body = objectMapper.writeValueAsString(request);

        //when,then
        mockMvc.perform(post("/schedule/normal")
                        .header("Authorization","Bearer " + token) // JWT 쿠키 추가
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("일반일정을 단건 수정한다.")
    public void update() throws Exception {
        //given
        Map<String, Object> request = new HashMap<>();
        request.put("id", 1);
        request.put("title", "Test ScheduleEntity");
        request.put("commonDescription", "Test Description");
        String body = objectMapper.writeValueAsString(request);

        //when,then
        mockMvc.perform(put("/schedule/normal")
                        .header("Authorization","Bearer " + token) // JWT 쿠키 추가
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("일반일정 자식일정을 단건 수정한다.")
    public void updateDetail() throws Exception {
        //given
        Map<String, Object> request = new HashMap<>();
        request.put("id", 1);
        request.put("detailDescription", "Test Description");

        String body = objectMapper.writeValueAsString(request);

        //when,then
        mockMvc.perform(put("/schedule/normal/detail")
                .header("Authorization","Bearer " + token)
                .contentType(APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("특정 날짜 이후의 일반 자식일정을 벌크 삭제한다.")
    public void delete_detail_greater_than_equal_start() throws Exception {
        String parentId = "4";
        String startDate = LocalDateTime.of(2024, 5, 2, 10, 0).toString();

        mockMvc.perform(delete("/schedule/normal/detail")
                .param("parentId", parentId)
                .param("startDate", startDate)
                .header("Authorization","Bearer " + token)
        ).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("일반일정 자식일정 단건 삭제한다.")
    public void deleteDetailById() throws Exception {
        Long detailId = 2L;

        mockMvc.perform(delete("/schedule/normal/detail/{id}", detailId)
                .header("Authorization","Bearer " + token)
        ).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("일반일정 자식일정의 성취량을 업데이트 한다.")
    public void updateProgress() throws Exception {
        //given
        Map<String, Object> request = new HashMap<>();
        request.put("date", LocalDate.of(2024, 5, 1).toString());

        List<Map<String, Object>> progressUpdateRequests = List.of(
                Map.of(
                        "scheduleDetailId", 1L,
                        "completeStatus", true,
                        "achievedAmount", 10
                )
        );

        request.put("progressUpdateRequests", progressUpdateRequests);

        String body = objectMapper.writeValueAsString(request);

        //when,then
        mockMvc.perform(patch("/schedule/normal/progress")
                .header("Authorization","Bearer " + token)
                .content(body)
                .contentType(APPLICATION_JSON)
        ).andExpect(status().isNoContent());
    }
}