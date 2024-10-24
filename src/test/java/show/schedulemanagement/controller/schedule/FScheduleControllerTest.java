package show.schedulemanagement.controller.schedule;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockCookie;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.dto.schedule.request.fschedule.FScheduleDetailUpdate;
import show.schedulemanagement.dto.schedule.request.fschedule.FScheduleSave;
import show.schedulemanagement.dto.schedule.request.fschedule.FScheduleUpdate;
import show.schedulemanagement.dto.auth.LoginMemberDto;
import show.schedulemanagement.utils.TokenProvider;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@DisplayName("고정일정 엔드포인트")
@Transactional
class FScheduleControllerTest {

    @Autowired
    MockMvc mockMvc ;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    TokenProvider tokenProvider;  // TokenProvider 주입받기

    String token;
    MockCookie jwtCookie;

    @BeforeEach
    void setToken(){
        token = tokenProvider.generateJwtToken(new LoginMemberDto("normal@example.com")); // 토큰 생성
        jwtCookie = new MockCookie("jwt", token); // JWT 쿠키 생성
    }

    @Test
    @DisplayName("고정일정 정상 생성 - weekly")
    void add_weekly() throws Exception {
        FScheduleSave fScheduleSave = FScheduleSave.builder()
                .title("테스트 title ")
                .commonDescription("테스트 commonDescription")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(2))
                .frequency("weekly")
                .startTime(LocalTime.of(8,0))
                .duration(LocalTime.of(2, 0))
                .days(new ArrayList<>(List.of("mo", "we","fr")))
                .build();

        String dto = objectMapper.writeValueAsString(fScheduleSave);

        mockMvc.perform(post("/schedule/fixed")
                        .cookie(jwtCookie) // JWT 쿠키 추가
                        .content(dto)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("고정일정 정상 생성 - daily")
    void add_daily() throws Exception {
        FScheduleSave fScheduleSave = FScheduleSave.builder()
                .title("테스트 title ")
                .commonDescription("테스트 commonDescription")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(2))
                .frequency("daily")
                .startTime(LocalTime.of(8,0))
                .duration(LocalTime.of(2, 0))
                .days(new ArrayList<>(List.of("mo","tu","we","th","fr","sa","su")))
                .build();

        String dto = objectMapper.writeValueAsString(fScheduleSave);

        mockMvc.perform(post("/schedule/fixed")
                        .cookie(jwtCookie) // JWT 쿠키 추가
                        .content(dto)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("특정 날짜 이후의 고정일정 자식일정 벌크 삭제")
    public void delete_detail_greater_than_equal_start() throws Exception {
        String parentId = "1";
        String startDate = LocalDateTime.of(2024, 1, 1, 15, 30).toString();

        mockMvc.perform(delete("/schedule/fixed/detail")
                .param("parentId", parentId)
                .param("startDate", startDate)
                .cookie(jwtCookie)
        ).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("고정일정 자식일정 단건 삭제")
    public void delete_detail() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/schedule/fixed/detail/{id}",id)
                .cookie(jwtCookie)
        ).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("고정일정 부모일정 수정")
    public void update() throws Exception {
        Long id = 1L;
        String title = "고정일정 제목 수정";
        String commonDescription = "고정일정 메모 수정";
        FScheduleUpdate fScheduleUpdate = FScheduleUpdate.builder()
                .id(id)
                .title(title)
                .commonDescription(commonDescription)
                .build();

        String dto = objectMapper.writeValueAsString(fScheduleUpdate);

        mockMvc.perform(put("/schedule/fixed")
                .cookie(jwtCookie)
                .contentType(APPLICATION_JSON)
                .content(dto)
        ).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("고정일정 자식일정 단건 수정")
    void updateDetail() throws Exception {
        Long id = 1L;
        LocalDateTime startDate = LocalDateTime.of(2024, 9, 9, 10, 30);
        LocalDateTime endDate = LocalDateTime.of(2024, 9, 9, 12, 30);
        String detailDescription = "수정된 메모";
        FScheduleDetailUpdate fScheduleDetailUpdate = FScheduleDetailUpdate.builder()
                .id(id)
                .detailDescription(detailDescription)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        String dto = objectMapper.writeValueAsString(fScheduleDetailUpdate);

        mockMvc.perform(patch("/schedule/fixed/detail")
                .cookie(jwtCookie)
                .contentType(APPLICATION_JSON_VALUE)
                .content(dto)
        ).andExpect(status().isNoContent());
    }
}