package com.eighttoten.schedule.presentation;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.eighttoten.global.CurrentMember;
import com.eighttoten.global.ValidationSequence;
import com.eighttoten.member.domain.Member;
import com.eighttoten.schedule.domain.VSchedule;
import com.eighttoten.schedule.dto.request.VScheduleSaveRequestRequest;
import com.eighttoten.schedule.dto.request.VScheduleUpdateRequest;
import com.eighttoten.schedule.dto.response.ScheduleResponse;
import com.eighttoten.schedule.service.ScheduleService;
import com.eighttoten.schedule.service.VScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule/variable")
public class VScheduleController {
    private final ScheduleService scheduleService;
    private final VScheduleService vScheduleService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ScheduleResponse> add(
            @CurrentMember Member member,
            @RequestBody @Validated(value = ValidationSequence.class) VScheduleSaveRequestRequest dto)
    {
        VSchedule schedule = VSchedule.from(member, dto);
        scheduleService.save(schedule);
        return new ResponseEntity<>(ScheduleResponse.from(schedule,null), CREATED);
    }

    @PutMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> update(
            @CurrentMember Member member,
            @RequestBody @Validated(value = ValidationSequence.class) VScheduleUpdateRequest dto)
    {
        vScheduleService.update(member, dto);
        return ResponseEntity.noContent().build();
    }
}