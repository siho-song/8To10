package com.eighttoten.member;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("회원가입 컨트롤러 통합 테스트")
class SignUpControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("회원가입에 성공한다.")
    void 회원가입_성공() throws Exception {
        mockMvc.perform(post("/signup")
                        .content("username=송시호&nickname=쇼쇼&email=thdtlgh234@naver.com&password=aksdf124124!&gender=MALE&mode=SPICY&isAuthEmail=true&isAuthPhone=true&phoneNumber=01099921438&wakeUpTime=08:00:00&bedTime=08:00:00")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("이메일의 형식이 잘못되면 회원가입에 실패한다.")
    void 회원가입_실패_이메일_형식() throws Exception {
        mockMvc.perform(post("/signup")
                        .content("username=송시호&nickname=쇼쇼&email=invalid-email&password=aksdf124124!&gender=MALE&mode=SPICY&authEmail=true&authPhone=true&wakeUpTime=0000-00-00T08:00:00.000000&bedTime=0000-00-00T00:00:00.000000")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("비밀번호가 짧으면 회원가입에 실패한다.")
    void 회원가입_실패_비밀번호_짧음() throws Exception {
        mockMvc.perform(post("/signup")
                        .content("username=송시호&nickname=쇼쇼&email=thdtlgh234@naver.com&password=short&gender=MALE&mode=SPICY&authEmail=true&authPhone=true&wakeUpTime=0000-00-00T08:00:00.000000&bedTime=0000-00-00T00:00:00.000000")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("빈 필드가 있을경우 회원가입에 실패한다.")
    void 회원가입_실패_빈_필드() throws Exception {
        mockMvc.perform(post("/signup")
                        .content("username=&nickname=쇼쇼&email=thdtlgh234@naver.com&password=aksdf124124!&gender=MALE&mode=SPICY&authEmail=true&authPhone=true")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("닉네임의 중복을 확인한다.")
    void 닉네임_중복_확인() throws Exception {
        //given
        String nickname = "Nick1";

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/signup/nickname/exists")
                .contentType(MediaType.APPLICATION_JSON)
                .param("nickname",nickname)
        );

        //then
        result.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("false"));
    }

    @Test
    @DisplayName("이메의 중복을 확인한다.")
    void 이메일_중복_확인() throws Exception {
        //given
        String email = "normal@example.com";

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/signup/email/exists")
                .contentType(MediaType.APPLICATION_JSON)
                .param("email", email)
        );

        //then
        result.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));
    }
}