package show.schedulemanagement.controller.schedule;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
import show.schedulemanagement.security.dto.LoginMemberDto;
import show.schedulemanagement.security.utils.TokenUtils;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("일정 공통 컨트롤러 테스트")
@Transactional
class ScheduleControllerTest {

    @Autowired
    TokenUtils tokenUtils;

    @Autowired
    MockMvc mockMvc;

    MockCookie jwtCookie;
    String token;

    @BeforeEach
    void init(){
        token = tokenUtils.generateJwtToken(new LoginMemberDto("normal@example.com")); // 토큰 생성
        jwtCookie = new MockCookie("jwt", token); // JWT 쿠키 생성
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