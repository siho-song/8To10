package com.eighttoten.controller.schedule;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.eighttoten.config.web.CurrentMember;
import com.eighttoten.domain.member.Member;
import com.eighttoten.domain.schedule.Schedule;
import com.eighttoten.dto.Result;
import com.eighttoten.dto.schedule.response.ScheduleResponse;
import com.eighttoten.service.schedule.ScheduleService;
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