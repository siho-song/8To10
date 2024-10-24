package show.schedulemanagement.controller.schedule;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockCookie;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.dto.auth.LoginMemberDto;
import show.schedulemanagement.utils.TokenProvider;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("일정 공통 컨트롤러 테스트")
@Transactional
class ScheduleControllerTest {

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    MockMvc mockMvc;

    MockCookie jwtCookie;
    String token;

    @BeforeEach
    void init(){
        token = tokenProvider.generateJwtToken(new LoginMemberDto("normal@example.com")); // 토큰 생성
        jwtCookie = new MockCookie("jwt", token); // JWT 쿠키 생성
    }

    @Test
    @DisplayName("모든일정 조회")
    void getAllSchedule() throws Exception {
        mockMvc.perform(get("/schedule")
                        .cookie(jwtCookie))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("고정일정 삭제")
    void delete_FSchedule() throws Exception {
        mockMvc.perform(delete("/schedule/{id}", 1)
                        .cookie(jwtCookie))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("일반일정 삭제")
    void delete_NSchedule() throws Exception {
        mockMvc.perform(delete("/schedule/{id}", 4)
                        .cookie(jwtCookie))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("변동일정 삭제")
    void delete_VSchedule() throws Exception {
        mockMvc.perform(delete("/schedule/{id}", 7)
                        .cookie(jwtCookie))
                .andExpect(status().isNoContent());
    }
}