package com.eighttoten.schedule;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.eighttoten.member.domain.Member;
import com.eighttoten.schedule.domain.ScheduleAble;
import com.eighttoten.schedule.dto.response.ScheduleResponse;
import com.eighttoten.schedule.service.ScheduleAbleService;
import com.eighttoten.support.CurrentMember;
import com.eighttoten.support.Result;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule")
public class ScheduleController {
    private final ScheduleAbleService scheduleAbleService;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Result<ScheduleResponse>> getAllSchedule(@CurrentMember Member member){
        List<ScheduleAble> schedules = scheduleAbleService.findAllWithParentByMember(member);
        Result<ScheduleResponse> result = Result.fromElements(schedules, ScheduleResponse::from);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}