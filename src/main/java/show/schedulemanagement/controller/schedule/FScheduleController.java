package show.schedulemanagement.controller.schedule;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.fSchedule.FSchedule;
import show.schedulemanagement.dto.Result;
import show.schedulemanagement.dto.schedule.request.fSchedule.FScheduleSave;
import show.schedulemanagement.dto.schedule.response.ScheduleResponseDto;
import show.schedulemanagement.service.MemberService;
import show.schedulemanagement.service.schedule.ScheduleService;
import show.schedulemanagement.service.schedule.fSchedule.FScheduleDetailService;
import show.schedulemanagement.service.schedule.fSchedule.FScheduleService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule/fixed")
@Slf4j
public class FScheduleController {
    private final ScheduleService scheduleService;
    private final FScheduleService fScheduleService;
    private final FScheduleDetailService fScheduleDetailService;
    private final MemberService memberService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Result<ScheduleResponseDto>> add(@RequestBody @Valid FScheduleSave dto) {
        Member member = memberService.getAuthenticatedMember();
        FSchedule fSchedule = FSchedule.createFSchedule(member, dto);

        fScheduleService.addDetailsForEachEvent(fSchedule, dto.getEvents());
        scheduleService.save(fSchedule);

        Result<ScheduleResponseDto> result = new Result<>();
        scheduleService.setResultFromSchedule(result, fSchedule);

        return new ResponseEntity<>(result, CREATED);
    }

    @DeleteMapping(value = "/detail")
    public ResponseEntity<Integer> deleteDetailsGEStartDate(@RequestParam(value = "parentId") Long parentId,
                                                         @RequestParam(value = "startDate") LocalDateTime startDate) {
        Member member = memberService.getAuthenticatedMember();
        FSchedule parent = fScheduleService.findById(parentId);
        int deletedCount = fScheduleDetailService.deleteByStartDateGEAndMemberAndParent(startDate, member, parent);
        return new ResponseEntity<>(deletedCount, OK);
    }

    @DeleteMapping(value = "/detail/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> deleteDetail(@PathVariable(value = "id") Long id){
        Member member = memberService.getAuthenticatedMember();
        fScheduleDetailService.deleteById(member,id);
        return new ResponseEntity<>(id, OK);
    }
}
