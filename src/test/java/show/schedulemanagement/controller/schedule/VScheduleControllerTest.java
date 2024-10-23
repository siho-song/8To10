package show.schedulemanagement.controller.schedule;

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
import org.springframework.mock.web.MockCookie;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.dto.schedule.request.vschedule.VScheduleAdd;
import show.schedulemanagement.dto.schedule.request.vschedule.VScheduleUpdate;
import show.schedulemanagement.dto.auth.LoginMemberDto;
import show.schedulemanagement.utils.TokenUtils;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("변동일정 엔드포인트 테스트")
@Transactional
class VScheduleControllerTest {

    @Autowired
    TokenUtils tokenUtils;  // TokenUtils 주입받기

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    String token;
    MockCookie jwtCookie;

    @BeforeEach
    void setToken(){
        token = tokenUtils.generateJwtToken(new LoginMemberDto("normal@example.com")); // 토큰 생성
        jwtCookie = new MockCookie("jwt", token); // JWT 쿠키 생성
    }

    @Test
    @DisplayName("변동일정 생성 엔드포인트 정상 작동")
    void add() throws Exception {

        VScheduleAdd dto = VScheduleAdd.builder()
                .title("Test Schedule")
                .commonDescription("Test Description")
                .start(LocalDateTime.of(2024, 9, 1, 10, 0))
                .end(LocalDateTime.of(2024, 9, 1, 12, 0))
                .build();

        mockMvc.perform(post("/schedule/variable")
                        .cookie(jwtCookie) // JWT 쿠키 추가
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("변동일정 수정 엔드포인트 정상 작동")
    void update() throws Exception {

        VScheduleUpdate vScheduleUpdate = new VScheduleUpdate();
        vScheduleUpdate.setId(7L);
        vScheduleUpdate.setTitle("수정된 변동일정");
        vScheduleUpdate.setCommonDescription("수정된 변동일정 입니다.");
        vScheduleUpdate.setStartDate(LocalDateTime.now());
        vScheduleUpdate.setEndDate(LocalDateTime.now().plusHours(2L));

        String dto = objectMapper.writeValueAsString(vScheduleUpdate);

        mockMvc.perform(put("/schedule/variable")
                .cookie(jwtCookie)
                .contentType(APPLICATION_JSON)
                .content(dto)
        ).andExpect(status().isNoContent());
    }
}