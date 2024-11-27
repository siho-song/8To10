package com.eighttoten.controller.schedule;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.eighttoten.config.web.CurrentMember;
import com.eighttoten.domain.member.Member;
import com.eighttoten.domain.schedule.vschedule.VSchedule;
import com.eighttoten.dto.schedule.request.vschedule.VScheduleSave;
import com.eighttoten.dto.schedule.request.vschedule.VScheduleUpdate;
import com.eighttoten.dto.schedule.response.ScheduleResponse;
import com.eighttoten.service.schedule.ScheduleService;
import com.eighttoten.service.schedule.vschedule.VScheduleService;
import com.eighttoten.validator.ValidationSequence;
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
            @RequestBody @Validated(value = ValidationSequence.class) VScheduleSave dto)
    {
        VSchedule schedule = VSchedule.from(member, dto);
        scheduleService.save(schedule);
        return new ResponseEntity<>(ScheduleResponse.from(schedule,null), CREATED);
    }

    @PutMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> update(
            @CurrentMember Member member,
            @RequestBody @Validated(value = ValidationSequence.class) VScheduleUpdate dto)
    {
        vScheduleService.update(member, dto);
        return ResponseEntity.noContent().build();
    }
}