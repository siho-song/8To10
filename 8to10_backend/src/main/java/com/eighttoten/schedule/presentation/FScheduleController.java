package com.eighttoten.schedule.presentation;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.eighttoten.global.CurrentMember;
import com.eighttoten.global.ValidationSequence;
import com.eighttoten.global.dto.Result;
import com.eighttoten.member.domain.Member;
import com.eighttoten.schedule.domain.FSchedule;
import com.eighttoten.schedule.dto.request.FScheduleSaveRequestRequest;
import com.eighttoten.schedule.dto.request.FScheduleUpdateRequest;
import com.eighttoten.schedule.dto.request.FixDetailUpdateRequest;
import com.eighttoten.schedule.dto.response.ScheduleResponse;
import com.eighttoten.schedule.service.FScheduleDetailService;
import com.eighttoten.schedule.service.FScheduleService;
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
@RequiredArgsConstructor
@RequestMapping("/schedule/fixed")
public class FScheduleController {
    private final ScheduleService scheduleService;
    private final FScheduleService fScheduleService;
    private final FScheduleDetailService fScheduleDetailService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Result<ScheduleResponse>> add(
            @CurrentMember Member member,
            @RequestBody @Validated(value = ValidationSequence.class) FScheduleSaveRequestRequest dto) {

        FSchedule fSchedule = FSchedule.from(member, dto);

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
            @RequestBody @Valid FScheduleUpdateRequest fScheduleUpdateRequest) {

        fScheduleService.update(member, fScheduleUpdateRequest);
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
            @RequestBody @Validated(value = ValidationSequence.class) FixDetailUpdateRequest dto){

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