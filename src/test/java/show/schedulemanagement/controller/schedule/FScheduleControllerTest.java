package show.schedulemanagement.controller.schedule;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import show.schedulemanagement.dto.schedule.request.fSchedule.FScheduleDetailSave;
import show.schedulemanagement.dto.schedule.request.fSchedule.FScheduleSave;
import show.schedulemanagement.security.dto.LoginMemberDto;
import show.schedulemanagement.security.utils.TokenUtils;

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
    TokenUtils tokenUtils;  // TokenUtils 주입받기

    String token;
    MockCookie jwtCookie;

    @BeforeEach
    void setToken(){
        token = tokenUtils.generateJwtToken(new LoginMemberDto("normal@example.com")); // 토큰 생성
        jwtCookie = new MockCookie("jwt", token); // JWT 쿠키 생성
    }


    @Test
    @DisplayName("고정일정 정상 생성 - weekly, daily")
    void add_weekly() throws Exception {

        List<FScheduleDetailSave> events = new ArrayList<>();
        events.add(createFScheduleDetailSave(LocalTime.now(), LocalTime.of(2, 0), "weekly",
                new ArrayList<>(List.of("mo", "we", "fr"))));
        events.add(createFScheduleDetailSave(LocalTime.now().plusHours(3), LocalTime.of(2, 0), "daily",
                new ArrayList<>(List.of("mo","tu", "we","th","fr","sa","su"))));

        FScheduleSave fScheduleSave = FScheduleSave.builder()
                .title("테스트 title ")
                .commonDescription("테스트 commonDescription")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(2))
                .events(events)
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
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("고정일정 자식일정 단건 삭제")
    public void delete_detail() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/schedule/fixed/detail/{id}",id)
                .cookie(jwtCookie)
        ).andExpect(status().isOk());
    }

    private FScheduleDetailSave createFScheduleDetailSave(LocalTime startTime, LocalTime duration, String frequency, List<String> days) {
        return FScheduleDetailSave.builder()
                .startTime(startTime)
                .duration(duration)
                .frequency(frequency)
                .days(days)
                .build();
    }
}