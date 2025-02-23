package com.eighttoten.schedule;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
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

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("고정일정 컨트롤러 통합테스트")
class FScheduleControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    TokenProvider tokenProvider;

    String token;

    @BeforeEach
    void init(){
        token = tokenProvider.generateAccessToken("normal@example.com");
    }

    @Test
    @DisplayName("고정일정의 생성주기가 '주간' 일 때 생성에 성공한다.")
    void add_weekly() throws Exception {
        //given
        Map<String, Object> request = new HashMap<>();
        request.put("title", "테스트 title");
        request.put("commonDescription", "테스트 commonDescription");
        request.put("startDateTime", LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0)).toString());
        request.put("endDateTime", LocalDateTime.of(LocalDate.now().plusMonths(2), LocalTime.of(0, 0)).toString());
        request.put("frequency", "weekly");
        request.put("startTime", LocalTime.of(8,0).toString());
        request.put("duration", LocalTime.of(2, 0).toString());
        request.put("days", List.of("mo", "we", "fr"));
        String body = objectMapper.writeValueAsString(request);

        //when,then
        mockMvc.perform(post("/schedule/fixed")
                        .header("Authorization","Bearer " + token) // JWT 쿠키 추가
                        .content(body)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("고정일정의 생성주기가 '매일' 일때 생성에 성공한다.")
    void add_daily() throws Exception {
        //given
        Map<String, Object> request = new HashMap<>();
        request.put("title", "테스트 title");
        request.put("commonDescription", "테스트 commonDescription");
        request.put("startDateTime", LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0)).toString());
        request.put("endDateTime", LocalDateTime.of(LocalDate.now().plusMonths(2), LocalTime.of(0, 0)).toString());
        request.put("frequency", "daily");
        request.put("startTime", LocalTime.of(8,0).toString());
        request.put("duration", LocalTime.of(2, 0).toString());
        request.put("days", List.of("mo", "tu", "we", "th", "fr", "sa", "su"));
        String body = objectMapper.writeValueAsString(request);

        //when,then
        mockMvc.perform(post("/schedule/fixed")
                        .header("Authorization","Bearer " + token) // JWT 쿠키 추가
                        .content(body)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("특정 날짜 이후의 고정일정 벌크 삭제에 성공한다")
    public void delete_ById_detail_greater_than_equal_start() throws Exception {
        //given
        String parentId = "4";
        String startDate = LocalDateTime.of(2024, 1, 1, 15, 30).toString();

        //when,then
        mockMvc.perform(delete("/schedule/fixed/detail")
                .param("parentId", parentId)
                .param("startDate", startDate)
                .header("Authorization","Bearer " + token)
        ).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("고정일정 자식일정 단건 삭제에 성공한다.")
    public void delete_ById_detail() throws Exception {
        //given
        Long detailId = 1L;

        //when,then
        mockMvc.perform(delete("/schedule/fixed/detail/{id}",detailId)
                .header("Authorization","Bearer " + token)
        ).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("고정일정 부모일정 수정에 성공한다.")
    public void update_FSchedule() throws Exception {
        //given
        Long fScheduleId = 1L;
        String title = "고정일정 제목 수정";
        String commonDescription = "고정일정 메모 수정";

        Map<String, Object> request = new HashMap<>();
        request.put("id",fScheduleId);
        request.put("title",title);
        request.put("commonDescription", commonDescription);

        String body = objectMapper.writeValueAsString(request);

        //when,then
        mockMvc.perform(put("/schedule/fixed")
                .header("Authorization","Bearer " + token)
                .contentType(APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("고정일정 자식일정 단건 수정에 성공한다.")
    void updateDetail() throws Exception {
        //given
        Long detailId = 12L;
        LocalDateTime startDate = LocalDateTime.of(2024, 9, 9, 10, 30);
        LocalDateTime endDate = LocalDateTime.of(2024, 9, 9, 12, 30);
        String detailDescription = "수정된 메모";
        Map<String, Object> request = new HashMap<>();
        request.put("id",detailId);
        request.put("detailDescription",detailDescription);
        request.put("startDateTime", startDate);
        request.put("endDateTime", endDate);
        String body = objectMapper.writeValueAsString(request);

        //when,then
        mockMvc.perform(patch("/schedule/fixed/detail")
                .header("Authorization","Bearer " + token)
                .contentType(APPLICATION_JSON_VALUE)
                .content(body)
        ).andExpect(status().isNoContent());
    }
}