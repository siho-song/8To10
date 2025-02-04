package com.eighttoten.schedule;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.eighttoten.support.CurrentMember;
import com.eighttoten.support.ValidationSequence;
import com.eighttoten.member.domain.Member;
import com.eighttoten.schedule.dto.request.vschedule.VScheduleSaveRequest;
import com.eighttoten.schedule.dto.request.vschedule.VScheduleUpdateRequest;
import com.eighttoten.schedule.service.vschedule.VScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule/variable")
public class VScheduleController {
    private final VScheduleService vScheduleService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> save(
            @CurrentMember Member member,
            @RequestBody @Validated(value = ValidationSequence.class) VScheduleSaveRequest request)
    {
        vScheduleService.save(request.toNewVSchedule(member.getId()));
        return ResponseEntity.noContent().build();
    }

    @PutMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> update(
            @CurrentMember Member member,
            @RequestBody @Validated(value = ValidationSequence.class) VScheduleUpdateRequest request)
    {
        vScheduleService.update(member, request.toVScheduleUpdate());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(
            @CurrentMember Member member,
            @PathVariable(value = "id") Long id)
    {
        vScheduleService.deleteById(member, id);
        return ResponseEntity.ok().build();
    }
}