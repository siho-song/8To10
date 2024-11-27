package com.eighttoten.domain.schedule.fschedule;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import com.eighttoten.domain.member.Member;
import com.eighttoten.domain.schedule.Schedule;
import com.eighttoten.domain.schedule.ScheduleAble;
import com.eighttoten.dto.schedule.request.fschedule.FScheduleSave;
import com.eighttoten.dto.schedule.request.fschedule.FScheduleUpdate;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
@DiscriminatorValue(value = "F")
@ToString(callSuper = true)
public class FSchedule extends Schedule{

    @OneToMany(mappedBy = "fSchedule" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<FScheduleDetail> fScheduleDetails = new ArrayList<>();

    public static FSchedule createFSchedule(Member member, FScheduleSave fixRequestDto){
        FSchedule fSchedule = new FSchedule();
        fSchedule.member = member;
        fSchedule.title = fixRequestDto.getTitle();
        fSchedule.commonDescription = fixRequestDto.getCommonDescription();
        fSchedule.startDate = LocalDateTime.of(fixRequestDto.getStartDate(),LocalTime.of(0,0));
        fSchedule.endDate = LocalDateTime.of(fixRequestDto.getEndDate(),LocalTime.of(0,0));
        return fSchedule;
    }

    @Override
    public List<ScheduleAble> getScheduleAbles() {
        return fScheduleDetails.stream().map(fScheduleDetail -> (ScheduleAble) fScheduleDetail).toList();
    }

    public void update(FScheduleUpdate dto){
        super.title = dto.getTitle();
        super.commonDescription = dto.getCommonDescription();
    }
}