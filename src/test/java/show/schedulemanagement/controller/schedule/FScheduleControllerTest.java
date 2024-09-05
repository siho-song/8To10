package show.schedulemanagement.controller.schedule;

import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockCookie;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.dto.schedule.request.FixAddDto;
import show.schedulemanagement.dto.schedule.request.FixDetailAddDto;
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

        List<FixDetailAddDto> events = new ArrayList<>();
        events.add(createFixDetailAddDto(LocalTime.now(), LocalTime.of(2, 0), "weekly",
                new ArrayList<>(List.of("mo", "we", "fr"))));
        events.add(createFixDetailAddDto(LocalTime.now().plusHours(3), LocalTime.of(2, 0), "daily",
                new ArrayList<>(List.of("mo","tu", "we","th","fr","sa","su"))));

        FixAddDto fixAddDto = FixAddDto.builder()
                .title("테스트 title ")
                .commonDescription("테스트 commonDescription")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(2))
                .events(events)
                .build();

        String dto = objectMapper.writeValueAsString(fixAddDto);

        mockMvc.perform(post("/schedule/fixed/add")
                        .cookie(jwtCookie) // JWT 쿠키 추가
                        .content(dto)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("고정일정 자식일정 단건 삭제")
    public void delete_detail() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/schedule/fixed/{id}",id)
                .cookie(jwtCookie)
        ).andExpect(status().isOk());
    }

    private FixDetailAddDto createFixDetailAddDto(LocalTime startTime, LocalTime duration, String frequency, List<String> days) {
        return FixDetailAddDto.builder()
                .startTime(startTime)
                .duration(duration)
                .frequency(frequency)
                .days(days)
                .build();
    }
}