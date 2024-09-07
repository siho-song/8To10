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
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.Schedule;
import show.schedulemanagement.domain.schedule.vSchedule.VSchedule;
import show.schedulemanagement.dto.schedule.request.vSchedule.VScheduleAdd;
import show.schedulemanagement.dto.schedule.request.vSchedule.VScheduleUpdate;
import show.schedulemanagement.dto.schedule.response.ScheduleResponseDto;
import show.schedulemanagement.service.MemberService;
import show.schedulemanagement.service.schedule.ScheduleService;
import show.schedulemanagement.service.schedule.vSchedule.VScheduleService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule/variable")
@Slf4j
public class VScheduleController {
    private final ScheduleService scheduleService;
    private final VScheduleService vScheduleService;
    private final MemberService memberService;

    @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ScheduleResponseDto> update(@RequestBody @Valid VScheduleUpdate dto){
        Member member = memberService.getAuthenticatedMember();
        vScheduleService.update(member, dto);
        Schedule schedule = scheduleService.findById(dto.getId());
        return new ResponseEntity<>(ScheduleResponseDto.from(schedule, null), OK);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ScheduleResponseDto> add(@RequestBody @Valid VScheduleAdd dto) {
        Member member = memberService.getAuthenticatedMember();
        VSchedule schedule = VSchedule.createVSchedule(member, dto);
        scheduleService.save(schedule);
        return new ResponseEntity<>(ScheduleResponseDto.from(schedule,null), CREATED);
    }
}
