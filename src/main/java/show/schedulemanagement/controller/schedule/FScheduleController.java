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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.fschedule.FSchedule;
import show.schedulemanagement.domain.schedule.fschedule.FScheduleDetail;
import show.schedulemanagement.dto.Result;
import show.schedulemanagement.dto.schedule.request.fschedule.FScheduleDetailUpdate;
import show.schedulemanagement.dto.schedule.request.fschedule.FScheduleSave;
import show.schedulemanagement.dto.schedule.request.fschedule.FScheduleUpdate;
import show.schedulemanagement.dto.schedule.response.fschedule.FScheduleDetailUpdateResponse;
import show.schedulemanagement.dto.schedule.response.fschedule.FScheduleUpdateResponse;
import show.schedulemanagement.dto.schedule.response.ScheduleResponse;
import show.schedulemanagement.service.MemberService;
import show.schedulemanagement.service.schedule.ScheduleService;
import show.schedulemanagement.service.schedule.fschedule.FScheduleDetailService;
import show.schedulemanagement.service.schedule.fschedule.FScheduleService;

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
    public ResponseEntity<Result<ScheduleResponse>> add(@RequestBody @Valid FScheduleSave dto) {
        Member member = memberService.getAuthenticatedMember();
        FSchedule fSchedule = FSchedule.createFSchedule(member, dto);

        fScheduleService.addDetails(fSchedule, dto);
        scheduleService.save(fSchedule);

        Result<ScheduleResponse> result = Result.fromElements(
                fSchedule.getScheduleAbles(),
                e -> ScheduleResponse.from(fSchedule, e)
        );

        return new ResponseEntity<>(result, CREATED);
    }

    @PutMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> update(@RequestBody @Valid FScheduleUpdate fScheduleUpdate) {
        Member member = memberService.getAuthenticatedMember();
        fScheduleService.update(member, fScheduleUpdate);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/detail")
    public ResponseEntity<Void> deleteDetailsGEStartDate(@RequestParam(value = "parentId") Long parentId,
                                                            @RequestParam(value = "startDate") LocalDateTime startDate) {
        Member member = memberService.getAuthenticatedMember();
        fScheduleDetailService.deleteByStartDateGEAndMemberAndParentId(startDate, member, parentId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/detail", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateDetail(@RequestBody @Valid FScheduleDetailUpdate dto){
        Member member = memberService.getAuthenticatedMember();
        fScheduleDetailService.update(member, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/detail/{id}")
    public ResponseEntity<Void> deleteDetail(@PathVariable(value = "id") Long id) {
        Member member = memberService.getAuthenticatedMember();
        fScheduleDetailService.deleteById(member, id);
        return ResponseEntity.noContent().build();
    }
}
