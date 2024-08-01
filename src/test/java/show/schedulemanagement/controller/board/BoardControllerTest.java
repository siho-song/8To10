package show.schedulemanagement.controller.board;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockCookie;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import show.schedulemanagement.dto.board.BoardRequestDto;
import show.schedulemanagement.security.dto.LoginMemberDto;
import show.schedulemanagement.security.utils.TokenUtils;

@SpringBootTest
@AutoConfigureMockMvc
class BoardControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    TokenUtils tokenUtils;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("Dto 검증에 통과한 게시물 객체는 정상 등록된다.")
    void boardAddTest() throws Exception {
        String token = tokenUtils.generateJwtToken(new LoginMemberDto("normal@example.com")); // 토큰 생성
        MockCookie jwtCookie = new MockCookie("jwt", token); // JWT 쿠키 생성
        BoardRequestDto dto = new BoardRequestDto("게시판 테스트용", "테스트......................");

        mockMvc.perform(MockMvcRequestBuilders.post("/community/add")
                        .cookie(jwtCookie) // JWT 쿠키 추가
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }
}