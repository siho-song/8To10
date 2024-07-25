package show.schedulemanagement.controller.schedule;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockCookie;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import show.schedulemanagement.dto.schedule.request.FixAddDto;
import show.schedulemanagement.dto.schedule.request.FixDetailAddDto;
import show.schedulemanagement.security.dto.LoginMemberDto;
import show.schedulemanagement.security.utils.TokenUtils;

@SpringBootTest
@AutoConfigureMockMvc
class FScheduleControllerTest {
    Logger logger = LoggerFactory.getLogger(FScheduleControllerTest.class);

    @Autowired
    MockMvc mockMvc ;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    TokenUtils tokenUtils;  // TokenUtils 주입받기


    @Test
    @DisplayName("올바른 형식의 고정일정 데이터를 삽입하면 테스트가 성공한다.")
    void addFSchedule() throws Exception {

        String token = tokenUtils.generateJwtToken(new LoginMemberDto("normal@example.com")); // 토큰 생성
        MockCookie jwtCookie = new MockCookie("jwt", token); // JWT 쿠키 생성

        List<FixDetailAddDto> events = new ArrayList<>();
        events.add(getFixDetailAddDto(LocalTime.now(), LocalTime.of(2, 0), "weekly",
                new ArrayList<>(List.of("mo", "we", "fr"))));
        events.add(getFixDetailAddDto(LocalTime.now(), LocalTime.of(2, 0), "weekly",
                new ArrayList<>(List.of("tu","th"))));

        FixAddDto fixAddDto = FixAddDto.builder()
                .title("테스트 title ")
                .commonDescription("테스트 commonDescription")
                .startDate(LocalDate.of(2024, 5, 26))
                .endDate(LocalDate.of(2024, 6, 7))
                .events(events)
                .build();

        String fixAddDtoJson = objectMapper.writeValueAsString(fixAddDto);
        logger.debug("addFSchedule Test request dto : {} ", fixAddDtoJson);

        //post 요청
        mockMvc.perform(post("/schedule/fixed/add")
                        .cookie(jwtCookie) // JWT 쿠키 추가
                        .content(fixAddDtoJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    private FixDetailAddDto getFixDetailAddDto(LocalTime startTime, LocalTime duration, String frequency, List<String> days) {
        return FixDetailAddDto.builder()
                .startTime(startTime)
                .duration(duration)
                .frequency(frequency)
                .days(days)
                .build();
    }
}