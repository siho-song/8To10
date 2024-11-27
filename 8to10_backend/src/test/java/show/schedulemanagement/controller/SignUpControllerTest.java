package show.schedulemanagement.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("회원가입 컨트롤러 테스트")
class SignUpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 회원가입_성공() throws Exception {
        mockMvc.perform(post("/signup")
                        .content("username=송시호&nickname=쇼쇼&email=thdtlgh234@naver.com&password=aksdf124124!&gender=MALE&mode=SPICY&authEmail=true&authPhone=true&phoneNumber=01099921438&wakeUpTime=08:00:00&bedTime=08:00:00")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isNoContent());
    }

    @Test
    void 회원가입_실패_이메일_형식() throws Exception {
        mockMvc.perform(post("/signup")
                        .content("username=송시호&nickname=쇼쇼&email=invalid-email&password=aksdf124124!&gender=MALE&mode=SPICY&authEmail=true&authPhone=true&wakeUpTime=0000-00-00T08:00:00.000000&bedTime=0000-00-00T00:00:00.000000")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 회원가입_실패_비밀번호_짧음() throws Exception {
        mockMvc.perform(post("/signup")
                        .content("username=송시호&nickname=쇼쇼&email=thdtlgh234@naver.com&password=short&gender=MALE&mode=SPICY&authEmail=true&authPhone=true&wakeUpTime=0000-00-00T08:00:00.000000&bedTime=0000-00-00T00:00:00.000000")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 회원가입_실패_빈_필드() throws Exception {
        mockMvc.perform(post("/signup")
                        .content("username=&nickname=쇼쇼&email=thdtlgh234@naver.com&password=aksdf124124!&gender=MALE&mode=SPICY&authEmail=true&authPhone=true")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isBadRequest());
    }
}