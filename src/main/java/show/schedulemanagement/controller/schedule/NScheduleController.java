package show.schedulemanagement.controller.schedule;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.nschedule.NSchedule;
import show.schedulemanagement.dto.schedule.request.nschedule.NScheduleDetailUpdate;
import show.schedulemanagement.dto.schedule.request.nschedule.NScheduleSave;
import show.schedulemanagement.dto.schedule.request.nschedule.NScheduleUpdate;
import show.schedulemanagement.dto.schedule.request.nschedule.ToDoUpdate;
import show.schedulemanagement.dto.schedule.response.ScheduleResponse;
import show.schedulemanagement.dto.Result;
import show.schedulemanagement.dto.schedule.response.nschedule.NScheduleUpdateResponse;
import show.schedulemanagement.service.MemberService;
import show.schedulemanagement.service.schedule.nschedule.NScheduleDetailService;
import show.schedulemanagement.service.schedule.nschedule.NScheduleService;
import show.schedulemanagement.service.schedule.ScheduleService;

@RestController
@RequestMapping("/schedule/normal")
@RequiredArgsConstructor
@Slf4j
public class NScheduleController {

    private final ScheduleService scheduleService;
    private final MemberService memberService;
    private final NScheduleService nScheduleService;
    private final NScheduleDetailService nScheduleDetailService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Result<ScheduleResponse>> add(@RequestBody @Valid NScheduleSave dto) {
        Member member = memberService.getAuthenticatedMember();
        NSchedule nSchedule = nScheduleService.create(member, dto);
        scheduleService.save(nSchedule);

        Result<ScheduleResponse> result = new Result<>();
        scheduleService.setResultFromSchedule(result, nSchedule);
        return new ResponseEntity<>(result, CREATED);
    }

    @PutMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> update(@RequestBody @Valid NScheduleUpdate dto){
        Member member = memberService.getAuthenticatedMember();
        nScheduleService.update(member, dto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/todo" , consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateTodo(@RequestBody List<ToDoUpdate> toDoUpdates){
        Member member = memberService.getAuthenticatedMember();
        nScheduleDetailService.updateCompleteStatuses(member, toDoUpdates);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/detail", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateDetail(@RequestBody @Valid NScheduleDetailUpdate dto){
        Member member = memberService.getAuthenticatedMember();
        nScheduleDetailService.update(member,dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/detail")
    public ResponseEntity<Void> deleteDetailsGEStartDate(
            @RequestParam(value = "startDate") LocalDateTime startDate,
            @RequestParam(value = "parentId") Long parentId)
    {
        Member member = memberService.getAuthenticatedMember();
        nScheduleDetailService.deleteByStartDateGEAndMemberAndParentId(
                startDate, member, parentId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/detail/{id}")
    public ResponseEntity<Void> deleteDetail(@PathVariable(value = "id") Long id){
        Member member = memberService.getAuthenticatedMember();
        nScheduleDetailService.deleteById(member, id);
        return ResponseEntity.noContent().build();
    }
}
