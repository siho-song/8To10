package show.schedulemanagement.service.schedule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.schedule.fSchedule.FSchedule;
import show.schedulemanagement.domain.schedule.fSchedule.FScheduleDetail;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class ScheduleDetailServiceImplTest {
    @Autowired
    ScheduleDetailService scheduleDetailService;

    @Autowired
    ScheduleService scheduleService;

    @Test
    @DisplayName(value = "고정일정의 자식일정을 삭제한다. 만약 고정일정의 자식의 크기가 0 이라면 해당 고정일정도 삭제한다.")
    @Transactional
    void deleteFdById() {
        FSchedule fSchedule = (FSchedule) scheduleService.findById(1L);
        List<FScheduleDetail> fScheduleDetails = fSchedule.getFScheduleDetails();
        assertThat(fScheduleDetails.size()).isEqualTo(6);
        List<Long> ids = fScheduleDetails.stream().map(FScheduleDetail::getId).toList();
        for (Long id : ids) {
            scheduleDetailService.deleteFdById(id);
        }
        assertThatThrownBy(() -> scheduleService.findById(1L)).isInstanceOf(RuntimeException.class);
    }
}