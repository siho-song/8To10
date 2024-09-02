package show.schedulemanagement.controller.schedule;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockCookie;
import org.springframework.test.web.servlet.MockMvc;
import show.schedulemanagement.dto.schedule.request.NormalAddDto;
import show.schedulemanagement.dto.schedule.request.VariableAddDto;
import show.schedulemanagement.security.dto.LoginMemberDto;
import show.schedulemanagement.security.utils.TokenUtils;

@SpringBootTest
@AutoConfigureMockMvc
class VScheduleControllerTest {

    @Autowired
    TokenUtils tokenUtils;  // TokenUtils 주입받기

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    void addSchedule() throws Exception {
        String token = tokenUtils.generateJwtToken(new LoginMemberDto("normal@example.com")); // 토큰 생성
        MockCookie jwtCookie = new MockCookie("jwt", token); // JWT 쿠키 생성

        VariableAddDto dto = VariableAddDto.builder()
                .title("Test Schedule")
                .commonDescription("Test Description")
                .start(LocalDateTime.of(2024, 9, 1, 10, 0))
                .end(LocalDateTime.of(2024, 9, 1, 12, 0))
                .build();

        mockMvc.perform(post("/schedule/variable/add")
                        .cookie(jwtCookie) // JWT 쿠키 추가
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }
}