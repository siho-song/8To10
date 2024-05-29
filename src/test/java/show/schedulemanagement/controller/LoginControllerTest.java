package show.schedulemanagement.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.member.Gender;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.member.Mode;
import show.schedulemanagement.repository.member.MemberRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 초기화
        Member member = Member.builder()
                .username("테스트")
                .nickname("테스트")
                .phoneNumber("01012341678")
                .gender(Gender.MALE)
                .authEmail(false)
                .authPhone(false)
                .startOfWork(LocalTime.now())
                .endOfWork(LocalTime.now())
                .mode(Mode.MILD)
                .email("test1@example.com")
                .password("$2a$12$vVyp1MKvgHaS68VKu/gyjeaFqHiXzKiu8Cq5A8jeoLZzHM900.0X2")
                .build();
        memberRepository.save(member);
    }

    @Test
    void 로그인_성공() throws Exception {
        // Given
        String email = "test1@example.com";
        String password = "password1";

        // When
        ResultActions resultActions = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", email)
                .param("password", password));

        // Then
        resultActions.andExpect(status().isFound()) // 302 Redirect
                .andExpect(header().exists("Set-Cookie")) // JWT 토큰이 포함된 쿠키가 존재하는지 확인
                .andExpect(header().string("Location", "/home")); // 리다이렉트 위치 확인
    }

    @Test
    void 로그인_실패_잘못된_비밀번호() throws Exception {
        // Given
        String email = "test1@example.com";
        String password = "wrongpassword";

        // When
        ResultActions resultActions = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", email)
                .param("password", password));

        // Then
        resultActions.andExpect(status().isUnauthorized());
    }
}
