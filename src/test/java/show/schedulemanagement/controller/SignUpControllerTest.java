package show.schedulemanagement.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class SignUpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 회원가입_성공() throws Exception {
        mockMvc.perform(post("/signup")
                        .content("username=송시호&nickname=쇼쇼&email=thdtlgh234@naver.com&password=aksdf124124!&gender=MALE&mode=SPICY&authEmail=true&authPhone=true&phoneNumber=01099921438")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void 회원가입_실패_이메일_형식() throws Exception {
        mockMvc.perform(post("/signup")
                        .content("username=송시호&nickname=쇼쇼&email=invalid-email&password=aksdf124124!&gender=MALE&mode=SPICY&authEmail=true&authPhone=true")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 회원가입_실패_비밀번호_짧음() throws Exception {
        mockMvc.perform(post("/signup")
                        .content("username=송시호&nickname=쇼쇼&email=thdtlgh234@naver.com&password=short&gender=MALE&mode=SPICY&authEmail=true&authPhone=true")
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