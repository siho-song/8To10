package show.schedulemanagement.controller.schedule;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import show.schedulemanagement.config.web.CurrentMember;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.nschedule.NSchedule;
import show.schedulemanagement.dto.Result;
import show.schedulemanagement.dto.schedule.request.nschedule.NScheduleDetailUpdate;
import show.schedulemanagement.dto.schedule.request.nschedule.NScheduleSave;
import show.schedulemanagement.dto.schedule.request.nschedule.NScheduleUpdate;
import show.schedulemanagement.dto.schedule.request.nschedule.ProgressUpdateRequest;
import show.schedulemanagement.dto.schedule.response.ScheduleResponse;
import show.schedulemanagement.service.schedule.ScheduleService;
import show.schedulemanagement.service.schedule.nschedule.NScheduleDetailService;
import show.schedulemanagement.service.schedule.nschedule.NScheduleService;
import show.schedulemanagement.validator.ValidationSequence;

@RestController
@RequestMapping("/schedule/normal")
@RequiredArgsConstructor
@Slf4j
public class NScheduleController {

    private final ScheduleService scheduleService;
    private final NScheduleService nScheduleService;
    private final NScheduleDetailService nScheduleDetailService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Result<ScheduleResponse>> add(
            @CurrentMember Member member,
            @RequestBody @Validated(value = ValidationSequence.class) NScheduleSave dto)
    {
        NSchedule nSchedule = nScheduleService.create(member, dto);
        scheduleService.save(nSchedule);

        Result<ScheduleResponse> result = Result.fromElements(
                nSchedule.getScheduleAbles(),
                e -> ScheduleResponse.from(nSchedule, e)
        );

        return new ResponseEntity<>(result, CREATED);
    }

    @PutMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> update(
            @CurrentMember Member member,
            @RequestBody @Valid NScheduleUpdate dto)
    {
        nScheduleService.update(member, dto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/progress", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateTodo(
            @CurrentMember Member member,
            @RequestBody @Valid ProgressUpdateRequest progressUpdateRequest)
    {
        nScheduleDetailService.updateProgress(member, progressUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/detail", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateDetail(
            @CurrentMember Member member,
            @RequestBody @Valid NScheduleDetailUpdate dto)
    {
        nScheduleDetailService.update(member,dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/detail")
    public ResponseEntity<Void> deleteDetailsGEStartDate(
            @CurrentMember Member member,
            @RequestParam(value = "startDate") LocalDateTime startDate,
            @RequestParam(value = "parentId") Long parentId)
    {
        nScheduleDetailService.deleteByStartDateGEAndMemberAndParentId(
                startDate, member, parentId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/detail/{id}")
    public ResponseEntity<Void> deleteDetail(
            @CurrentMember Member member,
            @PathVariable(value = "id") Long id){
        nScheduleDetailService.deleteById(member, id);
        return ResponseEntity.noContent().build();
    }
}
