package show.schedulemanagement.controller.schedule;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.dto.schedule.request.nschedule.NScheduleDetailUpdate;
import show.schedulemanagement.dto.schedule.request.nschedule.NScheduleSave;
import show.schedulemanagement.dto.schedule.request.nschedule.NScheduleUpdate;
import show.schedulemanagement.dto.schedule.request.nschedule.ProgressUpdateRequest;
import show.schedulemanagement.provider.TokenProvider;

@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
@Transactional
@DisplayName("일반일정 CRUD")
public class NScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private NScheduleSave nScheduleSave;
    private NScheduleUpdate nScheduleUpdate;

    @Autowired
    TokenProvider tokenProvider;

    String token;

    @BeforeEach
    public void init() {
        token = tokenProvider.generateAccessToken("normal@example.com"); // 토큰 생성

        nScheduleSave = NScheduleSave.builder()
                .title("Test Schedule")
                .commonDescription("Test Description")
                .startDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 4, 1))
                .bufferTime(LocalTime.of(1, 0))
                .performInDay(LocalTime.of(2, 0))
                .isIncludeSaturday(true)
                .isIncludeSunday(false)
                .totalAmount(10)
                .performInWeek(3)
                .build();

        nScheduleUpdate = NScheduleUpdate.builder()
                .id(4L)
                .title("수정된 제목")
                .commonDescription("수정된 메모")
                .build();
    }

    @Test
    @DisplayName("일반일정 정상 등록")
    @Rollback(value = false)
    public void add() throws Exception {
        mockMvc.perform(post("/schedule/normal")
                        .header("Authorization","Bearer " + token) // JWT 쿠키 추가
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nScheduleSave)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("일반일정 정상 수정")
    public void update() throws Exception {
        mockMvc.perform(put("/schedule/normal")
                        .header("Authorization","Bearer " + token) // JWT 쿠키 추가
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nScheduleUpdate)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("일반일정 자식일정 단건 수정")
    public void updateDetail() throws Exception {
        NScheduleDetailUpdate nScheduleDetailUpdate = NScheduleDetailUpdate.builder()
                .id(1L)
                .detailDescription("수정된 메모")
                .build();

        mockMvc.perform(put("/schedule/normal/detail")
                .header("Authorization","Bearer " + token)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nScheduleDetailUpdate))
        ).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("특정 날짜 이후의 일반일정 자식일정 벌크 삭제")
    public void delete_detail_greater_than_equal_start() throws Exception {
        String parentId = "4";
        String startDate = LocalDateTime.of(2024, 5, 2, 10, 0).toString();

        mockMvc.perform(delete("/schedule/normal/detail")
                .param("parentId", parentId)
                .param("startDate", startDate)
                .header("Authorization","Bearer " + token)
        ).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("일반일정 자식일정 단건 삭제")
    public void deleteDetailById() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/schedule/normal/detail/{id}", id)
                .header("Authorization","Bearer " + token)
        ).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("일반일정 자식일정 일정 진행상태 업데이트")
    public void updateProgress() throws Exception {
        ProgressUpdateRequest progressUpdateRequest = ProgressUpdateRequest.builder()
                .date(LocalDate.of(2024,5,1))
                .scheduleDetailId(1L)
                .isComplete(true)
                .achievedAmount(10)
                .build();

        String dto = objectMapper.writeValueAsString(progressUpdateRequest);

        mockMvc.perform(patch("/schedule/normal/progress")
                .header("Authorization","Bearer " + token)
                .content(dto)
                .contentType(APPLICATION_JSON)
        ).andExpect(status().isNoContent());
    }
}
