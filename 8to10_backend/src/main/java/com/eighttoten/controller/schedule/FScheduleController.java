package com.eighttoten.controller.schedule;

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
import com.eighttoten.config.web.CurrentMember;
import com.eighttoten.domain.member.Member;
import com.eighttoten.domain.schedule.fschedule.FSchedule;
import com.eighttoten.dto.Result;
import com.eighttoten.dto.schedule.request.fschedule.FScheduleDetailUpdate;
import com.eighttoten.dto.schedule.request.fschedule.FScheduleSave;
import com.eighttoten.dto.schedule.request.fschedule.FScheduleUpdate;
import com.eighttoten.dto.schedule.response.ScheduleResponse;
import com.eighttoten.service.schedule.ScheduleService;
import com.eighttoten.service.schedule.fschedule.FScheduleDetailService;
import com.eighttoten.service.schedule.fschedule.FScheduleService;
import com.eighttoten.validator.ValidationSequence;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule/fixed")
@Slf4j
public class FScheduleController {
    private final ScheduleService scheduleService;
    private final FScheduleService fScheduleService;
    private final FScheduleDetailService fScheduleDetailService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Result<ScheduleResponse>> add(
            @CurrentMember Member member,
            @RequestBody @Validated(value = ValidationSequence.class) FScheduleSave dto) {

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
    public ResponseEntity<Void> update(
            @CurrentMember Member member,
            @RequestBody @Valid FScheduleUpdate fScheduleUpdate) {

        fScheduleService.update(member, fScheduleUpdate);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/detail")
    public ResponseEntity<Void> deleteDetailsGEStartDate(
            @CurrentMember Member member,
            @RequestParam(value = "parentId") Long parentId,
            @RequestParam(value = "startDate") LocalDateTime startDate) {

        fScheduleDetailService.deleteByStartDateGEAndMemberAndParentId(startDate, member, parentId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/detail", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateDetail(
            @CurrentMember Member member,
            @RequestBody @Validated(value = ValidationSequence.class) FScheduleDetailUpdate dto){

        fScheduleDetailService.update(member, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/detail/{id}")
    public ResponseEntity<Void> deleteDetail(
            @CurrentMember Member member,
            @PathVariable(value = "id") Long id) {

        fScheduleDetailService.deleteById(member, id);
        return ResponseEntity.noContent().build();
    }
}
