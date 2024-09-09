package show.schedulemanagement.controller.schedule;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalTime;
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
import show.schedulemanagement.dto.schedule.request.nschedule.NScheduleSave;
import show.schedulemanagement.security.dto.LoginMemberDto;
import show.schedulemanagement.security.utils.TokenUtils;

@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
@Transactional
@DisplayName("일반일정 CRUD")
public class NScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private NScheduleSave nScheduleSave;

    @Autowired
    TokenUtils tokenUtils;  // TokenUtils 주입받기

    @BeforeEach
    public void setup() {
        nScheduleSave = NScheduleSave.builder()
                .title("Test Schedule")
                .commonDescription("Test Description")
                .startDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 4, 1))
                .bufferTime(LocalTime.of(1, 0))
                .performInDay(LocalTime.of(2, 0))
                .isIncludeSaturday(true)
                .isIncludeSunday(false)
                .totalAmount(10)
                .performInWeek(3)
                .build();
    }

    @Test
    @DisplayName("일반일정 정상 등록")
    public void add() throws Exception {
        log.debug("testAddSchedule called normal Add dto : {}", nScheduleSave);
        String token = tokenUtils.generateJwtToken(new LoginMemberDto("normal@example.com")); // 토큰 생성
        MockCookie jwtCookie = new MockCookie("jwt", token); // JWT 쿠키 생성

        mockMvc.perform(post("/schedule/normal")
                        .cookie(jwtCookie) // JWT 쿠키 추가
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nScheduleSave)))
                .andExpect(status().isCreated());
    }
}
