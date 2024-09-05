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
import show.schedulemanagement.domain.schedule.nSchedule.NSchedule;
import show.schedulemanagement.dto.schedule.request.NormalAddDto;
import show.schedulemanagement.dto.schedule.response.NormalResponseDto;
import show.schedulemanagement.dto.Result;
import show.schedulemanagement.service.MemberService;
import show.schedulemanagement.service.schedule.nSchedule.NScheduleService;
import show.schedulemanagement.service.schedule.ScheduleService;

@RestController
@RequestMapping("/schedule/normal")
@RequiredArgsConstructor
@Slf4j
public class NScheduleController {

    private final ScheduleService scheduleService;
    private final MemberService memberService;
    private final NScheduleService nScheduleService;

    @PostMapping(path = "/add",consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result<NormalResponseDto>> addSchedule(@RequestBody @Valid NormalAddDto dto) throws RuntimeException{
        Member member = memberService.getAuthenticatedMember();
        NSchedule nSchedule = nScheduleService.addNSchedule(member, dto);
        scheduleService.save(nSchedule);
        Result<NormalResponseDto> result = nScheduleService.getResult(nSchedule);
        log.debug("result : {} " , result);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}
