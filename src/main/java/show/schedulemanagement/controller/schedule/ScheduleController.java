package show.schedulemanagement.controller.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.dto.schedule.response.Result;
import show.schedulemanagement.dto.schedule.response.ScheduleResponseDto;
import show.schedulemanagement.service.MemberService;
import show.schedulemanagement.service.schedule.ScheduleService;

@RestController("/schedule")
@Slf4j
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final MemberService memberService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result<ScheduleResponseDto>> getAllSchedule(){
        Member member = memberService.getAuthenticatedMember();
        Result<ScheduleResponseDto> result = scheduleService.getResult(member);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> deleteSchedule(@PathVariable("id") Long id) throws Exception {
        scheduleService.deleteById(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
