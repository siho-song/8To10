package show.schedulemanagement.controller.schedule;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.vSchedule.VSchedule;
import show.schedulemanagement.dto.schedule.request.VariableAddDto;
import show.schedulemanagement.dto.schedule.response.ScheduleResponseDto;
import show.schedulemanagement.dto.schedule.response.VariableResponseDto;
import show.schedulemanagement.service.MemberService;
import show.schedulemanagement.service.schedule.ScheduleService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule/variable")
@Slf4j
public class VScheduleController {
    private final ScheduleService scheduleService;
    private final MemberService memberService;

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ScheduleResponseDto> addSchedule(@RequestBody @Valid VariableAddDto dto){

        //TODO 일정이 있으면 해당일정을 삭제하고 덮는다 .
        log.debug("VScheduleController addSchedule call VariableAddDto : {}", dto);
        Member member = memberService.getAuthenticatedMember();
        VSchedule schedule = VSchedule.createVSchedule(member, dto);
        scheduleService.save(schedule);
        return new ResponseEntity<>(new VariableResponseDto(schedule), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteSchedule(@PathVariable("id") Long id) throws Exception {
        scheduleService.deleteById(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
