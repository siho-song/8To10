package show.schedulemanagement.controller.schedule;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import show.schedulemanagement.config.web.CurrentMember;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.Schedule;
import show.schedulemanagement.domain.schedule.vschedule.VSchedule;
import show.schedulemanagement.dto.schedule.request.vschedule.VScheduleAdd;
import show.schedulemanagement.dto.schedule.request.vschedule.VScheduleUpdate;
import show.schedulemanagement.dto.schedule.response.ScheduleResponse;
import show.schedulemanagement.service.MemberService;
import show.schedulemanagement.service.schedule.ScheduleService;
import show.schedulemanagement.service.schedule.vschedule.VScheduleService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule/variable")
@Slf4j
public class VScheduleController {
    private final ScheduleService scheduleService;
    private final VScheduleService vScheduleService;
    private final MemberService memberService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ScheduleResponse> add(@CurrentMember Member member, @RequestBody @Valid VScheduleAdd dto) {
        VSchedule schedule = VSchedule.createVSchedule(member, dto);
        scheduleService.save(schedule);
        return new ResponseEntity<>(ScheduleResponse.from(schedule,null), CREATED);
    }

    @PutMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> update(@CurrentMember Member member, @RequestBody @Valid VScheduleUpdate dto){
        vScheduleService.update(member, dto);
        return ResponseEntity.noContent().build();
    }
}
