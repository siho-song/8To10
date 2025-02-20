package com.eighttoten.schedule;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.eighttoten.support.CurrentMember;
import com.eighttoten.support.ValidationSequence;
import com.eighttoten.member.domain.Member;
import com.eighttoten.schedule.dto.request.NScheduleSaveRequest;
import com.eighttoten.schedule.dto.request.NScheduleUpdateRequest;
import com.eighttoten.schedule.dto.request.NDetailUpdateRequest;
import com.eighttoten.schedule.dto.request.ProgressUpdatesRequest;
import com.eighttoten.schedule.service.nschedule.NScheduleDetailService;
import com.eighttoten.schedule.service.nschedule.NScheduleService;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    private final NScheduleService nScheduleService;
    private final NScheduleDetailService nScheduleDetailService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveWithDetails(
            @CurrentMember Member member,
            @RequestBody @Validated(value = ValidationSequence.class) NScheduleSaveRequest request)
    {
        nScheduleService.saveWithDetails(member, request.toNScheduleCreateInfo(),
                request.toNewNSchedule(member.getId()));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> update(
            @CurrentMember Member member,
            @RequestBody @Valid NScheduleUpdateRequest request)
    {
        nScheduleService.update(member, request.toNScheduleUpdate());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteById(
            @CurrentMember Member member,
            @PathVariable(value = "id") Long id)
    {
        nScheduleService.deleteById(member, id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/detail", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateDetail(
            @CurrentMember Member member,
            @RequestBody @Valid NDetailUpdateRequest request)
    {
        nScheduleDetailService.update(member, request.toNDetailUpdate());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/detail")
    public ResponseEntity<Void> deleteDetailsGEStartDate(
            @CurrentMember Member member,
            @RequestParam(value = "startDate") LocalDateTime startDate,
            @RequestParam(value = "parentId") Long parentId)
    {
        nScheduleDetailService.deleteAllByStartDateGEAndMemberAndParentId(
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

    @PatchMapping(value = "/progress", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateTodo(
            @CurrentMember Member member,
            @RequestBody @Valid ProgressUpdatesRequest request)
    {
        nScheduleDetailService.updateProgressList(member, request.toProgressUpdates());
        return ResponseEntity.noContent().build();
    }
}