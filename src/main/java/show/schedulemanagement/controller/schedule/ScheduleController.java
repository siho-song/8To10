package show.schedulemanagement.controller.schedule;

import static org.springframework.http.MediaType.*;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.Schedule;
import show.schedulemanagement.dto.Result;
import show.schedulemanagement.dto.schedule.response.ScheduleResponseDto;
import show.schedulemanagement.service.MemberService;
import show.schedulemanagement.service.schedule.ScheduleService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final MemberService memberService;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Result<ScheduleResponseDto>> getAllSchedule(){
        Member member = memberService.getAuthenticatedMember();
        List<Schedule> schedules = scheduleService.findAllWithDetailByMember(member);

        Result<ScheduleResponseDto> result = new Result<>();
        schedules.forEach(schedule -> scheduleService.setResultFromSchedule(result, schedule));

        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> deleteSchedule(@PathVariable("id") Long id) {
        Member member = memberService.getAuthenticatedMember();
        scheduleService.deleteById(member, id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
