package com.eighttoten.schedule.service;

import com.eighttoten.member.domain.Member;
import com.eighttoten.schedule.domain.ScheduleAble;
import com.eighttoten.schedule.domain.fschedule.repository.FScheduleDetailRepository;
import com.eighttoten.schedule.domain.nschedule.repository.NScheduleDetailRepository;
import com.eighttoten.schedule.domain.vschedule.repository.VScheduleRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleAbleService {
    private final NScheduleDetailRepository nScheduleDetailRepository;
    private final FScheduleDetailRepository fScheduleDetailRepository;
    private final VScheduleRepository vScheduleRepository;

    @Transactional(readOnly = true)
    public List<ScheduleAble> findAllBetweenStartAndEnd(Member member, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<ScheduleAble> allSchedules = new ArrayList<>();
        allSchedules.addAll(vScheduleRepository.findAllByEmailBetweenStartAndEnd(member.getEmail(), startDateTime, endDateTime));
        allSchedules.addAll(fScheduleDetailRepository.findAllByEmailBetweenStartAndEnd(member.getEmail(), startDateTime, endDateTime));
        allSchedules.addAll(nScheduleDetailRepository.findAllByEmailBetweenStartAndEnd(member.getEmail(), startDateTime, endDateTime));
        return allSchedules;
    }

    @Transactional(readOnly = true)
    public List<ScheduleAble> findAllWithParentByMember(Member member) {
        List<ScheduleAble> allSchedules = new ArrayList<>();
        allSchedules.addAll(vScheduleRepository.findAllByMemberEmail(member.getEmail()));
        allSchedules.addAll(fScheduleDetailRepository.findAllWithParentByMemberEmail(member.getEmail()));
        allSchedules.addAll(nScheduleDetailRepository.findAllWithParentByMemberEmail(member.getEmail()));
        return allSchedules;
    }
}