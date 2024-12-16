package com.eighttoten.schedule.presentation;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.eighttoten.global.CurrentMember;
import com.eighttoten.global.dto.Result;
import com.eighttoten.member.domain.Member;
import com.eighttoten.schedule.domain.Schedule;
import com.eighttoten.schedule.dto.response.ScheduleResponse;
import com.eighttoten.schedule.service.ScheduleService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Result<ScheduleResponse>> getAllSchedule(@CurrentMember Member member){
        List<Schedule> schedules = scheduleService.findAllWithDetailByMember(member);

        Result<ScheduleResponse> result = Result.fromElementsGroup(
                schedules,
                schedule -> Result.fromElements(
                        schedule.getScheduleAbles(),
                        e -> ScheduleResponse.from(schedule, e)
                ).getItems()
        );

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteSchedule(@CurrentMember Member member, @PathVariable("id") Long id) {
        scheduleService.deleteById(member, id);
        return ResponseEntity.noContent().build();
    }
}