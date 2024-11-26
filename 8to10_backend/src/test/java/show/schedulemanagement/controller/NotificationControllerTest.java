package show.schedulemanagement.controller;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import show.schedulemanagement.provider.TokenProvider;
import show.schedulemanagement.repository.notification.SseEmitterRepository;
import show.schedulemanagement.service.notification.SseEmitterService;

@AutoConfigureMockMvc
@SpringBootTest
class NotificationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    SseEmitterService sseEmitterService;

    @Autowired
    SseEmitterRepository sseEmitterRepository;

    @Autowired
    ObjectMapper objectMapper;

    String token;

    @BeforeEach
    void init() throws Exception {
        token = tokenProvider.generateAccessToken("normal@example.com"); // 토큰 생성
    }

    @AfterEach
    void after() {
        sseEmitterRepository.deleteAll();
    }

    @Test
    @DisplayName("SSE 연결에 성공한다.")
    public void subscribe() throws Exception {
        ResultActions result = mockMvc.perform(get("/notification/subscribe")
                .header("Authorization", "Bearer " + token)
                .accept(TEXT_EVENT_STREAM_VALUE)
        );

        result.andExpect(status().isOk());
    }

    @Test
    @DisplayName("알림을 삭제한다")
    public void deleteById() throws Exception {
        Long notificationId = 1L;
        mockMvc.perform(delete("/notification/{id}", notificationId)
                        .header("Authorization", "Bearer " + token)
                ).andExpect(status().isNoContent());
    }
}