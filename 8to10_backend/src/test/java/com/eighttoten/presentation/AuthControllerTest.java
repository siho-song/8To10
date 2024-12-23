package com.eighttoten.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.eighttoten.TestDataUtils;
import jakarta.servlet.http.Cookie;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import com.eighttoten.member.domain.Member;
import com.eighttoten.member.domain.repository.MemberRepository;
import com.eighttoten.auth.service.AuthService;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("인증 컨트롤러 테스트")
class AuthControllerTest {
    @MockBean
    MemberRepository memberRepository;

    @MockBean
    AuthService authService;

    @MockBean
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    MockMvc mockMvc;

    Optional<Member> member;

    @BeforeEach
    void init() {
        // 테스트 데이터 초기화
        member = Optional.of(TestDataUtils.createTestMember());
    }

    @Test
    @DisplayName("로그인에 성공하면 AccessToken, RefreshToken을 발급 받는다.")
    void loginSuccess() throws Exception {
        // given
        when(memberRepository.findByEmail(any())).thenReturn(member);
        when(bCryptPasswordEncoder.matches(any(), any())).thenReturn(true);

        // When
        ResultActions resultActions = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", "testEmail")
                .param("password", "testPassword"));

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(header().exists("Authorization")) // JWT 토큰이 포함된 쿠키가 존재하는지 확인
                .andExpect(cookie().exists("refresh_token"));
    }

    @Test
    @DisplayName("로그인에 실패하면 응답 상태코드로 401을 받는다.")
    void inValidPassword() throws Exception {
        // given
        when(memberRepository.findByEmail(any())).thenReturn(member);
        when(bCryptPasswordEncoder.matches(any(), any())).thenReturn(false);

        // When
        ResultActions resultActions = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", "test1@example.com")
                .param("password", "wrongpassword"));

        // Then
        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("유효한 RefreshToken과 만료된 AccessToken으로 새로운 AccessToken을 발급받는다.")
    void renewAccessToken() throws Exception {

        //given
        String renewAccessToken = "Renew AccessToken";
        String refreshToken = "validToken";
        String expiredAccessTokenHeader = "Bearer expiredToken";

        when(authService.getRenewalAccessToken(refreshToken, expiredAccessTokenHeader))
                .thenReturn(renewAccessToken);

        //when
        ResultActions resultActions = mockMvc.perform(post("/renew")
                .header("Authorization", expiredAccessTokenHeader)
                .cookie(new Cookie("refresh_token", refreshToken))
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.accessToken").value(renewAccessToken));
    }
}
