package com.eighttoten.schedule.presentation;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.eighttoten.global.CurrentMember;
import com.eighttoten.global.ValidationSequence;
import com.eighttoten.global.dto.Result;
import com.eighttoten.member.domain.Member;
import com.eighttoten.schedule.domain.NSchedule;
import com.eighttoten.schedule.dto.request.NScheduleSaveRequestRequest;
import com.eighttoten.schedule.dto.request.NScheduleUpdateRequest;
import com.eighttoten.schedule.dto.request.NormalDetailUpdateRequest;
import com.eighttoten.schedule.dto.request.ProgressUpdateRequest;
import com.eighttoten.schedule.dto.response.ScheduleResponse;
import com.eighttoten.schedule.service.NScheduleDetailService;
import com.eighttoten.schedule.service.NScheduleService;
import com.eighttoten.schedule.service.ScheduleService;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequestMapping("/schedule/normal")
@RequiredArgsConstructor
public class NScheduleController {
    private final ScheduleService scheduleService;
    private final NScheduleService nScheduleService;
    private final NScheduleDetailService nScheduleDetailService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Result<ScheduleResponse>> add(
            @CurrentMember Member member,
            @RequestBody @Validated(value = ValidationSequence.class) NScheduleSaveRequestRequest dto)
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
            @RequestBody @Valid NScheduleUpdateRequest dto)
    {
        nScheduleService.update(member, dto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/progress", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateTodo(
            @CurrentMember Member member,
            @RequestBody @Valid ProgressUpdateRequest progressUpdateRequest)
    {
        nScheduleDetailService.updateProgressList(member, progressUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/detail", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateDetail(
            @CurrentMember Member member,
            @RequestBody @Valid NormalDetailUpdateRequest dto)
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