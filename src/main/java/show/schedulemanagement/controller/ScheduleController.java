package show.schedulemanagement.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.vSchedule.VSchedule;
import show.schedulemanagement.dto.member.MemberDto;
import show.schedulemanagement.dto.schedule.request.VariableRequestDto;
import show.schedulemanagement.dto.schedule.response.ScheduleResponseDto;
import show.schedulemanagement.dto.schedule.response.VariableResponseDto;
import show.schedulemanagement.service.MemberService;
import show.schedulemanagement.service.schedule.ScheduleService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final MemberService memberService;

    @PostMapping("/schedule/variable/add")
    public ResponseEntity<ScheduleResponseDto> addSchedule(HttpSession session, @RequestBody VariableRequestDto dto){

        log.debug("ScheduleController addSchedule call VariableRequestDto : {}", dto);
        MemberDto memberDto = (MemberDto) session.getAttribute("member");
        Member member = memberService.findByEmail(memberDto.getEmail());
        VSchedule schedule = VSchedule.createVSchedule(member, dto);
        scheduleService.save(schedule);

        return new ResponseEntity<>(new VariableResponseDto(schedule), HttpStatus.CREATED);
    }

    @DeleteMapping("/schedule/variable/{id}")
    public ResponseEntity<Long> deleteSchedule(@PathVariable("id") Long id) {
        scheduleService.deleteById(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
