package show.schedulemanagement.controller.schedule;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.fSchedule.FSchedule;
import show.schedulemanagement.dto.schedule.request.FixAddDto;
import show.schedulemanagement.dto.Result;
import show.schedulemanagement.dto.schedule.response.ScheduleResponseDto;
import show.schedulemanagement.service.MemberService;
import show.schedulemanagement.service.schedule.fSchedule.FScheduleService;
import show.schedulemanagement.service.schedule.ScheduleService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule/fixed")
@Slf4j
public class FScheduleController {
    private final ScheduleService scheduleService;
    private final FScheduleService fScheduleService;
    private final MemberService memberService;

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result<ScheduleResponseDto>> add(@RequestBody @Valid FixAddDto dto) {
        Member member = memberService.getAuthenticatedMember();
        FSchedule fSchedule = FSchedule.createFSchedule(member, dto);

        fScheduleService.addDetailsForEachEvent(fSchedule, dto.getEvents());
        scheduleService.save(fSchedule);

        Result<ScheduleResponseDto> result = new Result<>();
        scheduleService.setResultFromSchedule(result, fSchedule);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}
