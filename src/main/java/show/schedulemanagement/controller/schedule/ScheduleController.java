package show.schedulemanagement.controller.schedule;

import static org.springframework.http.MediaType.*;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import show.schedulemanagement.config.web.CurrentMember;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.Schedule;
import show.schedulemanagement.dto.Result;
import show.schedulemanagement.dto.schedule.response.ScheduleResponse;
import show.schedulemanagement.service.MemberService;
import show.schedulemanagement.service.schedule.ScheduleService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Result<ScheduleResponse>> getAllSchedule(@CurrentMember Member member){
        List<Schedule> schedules = scheduleService.findAllWithDetailByMember(member);

        Result<ScheduleResponse> result = Result.fromElementsGroup(
                schedules,
                schedule -> Result.fromElements(
                        schedule.getScheduleAbles(),
                        e -> ScheduleResponse.from(schedule, e)
                ).getItems()
        );

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteSchedule(@CurrentMember Member member, @PathVariable("id") Long id) {
        scheduleService.deleteById(member, id);
        return ResponseEntity.noContent().build();
    }
}
