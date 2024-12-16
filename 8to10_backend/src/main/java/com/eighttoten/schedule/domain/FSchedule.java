package com.eighttoten.schedule.domain;

import static lombok.AccessLevel.PROTECTED;

import com.eighttoten.member.domain.Member;
import com.eighttoten.schedule.dto.request.FScheduleSaveRequestRequest;
import com.eighttoten.schedule.dto.request.FScheduleUpdateRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
@DiscriminatorValue(value = "F")
@Table(name = "f_schedule")
public class FSchedule extends Schedule{
    @OneToMany(mappedBy = "fSchedule" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<FScheduleDetail> fScheduleDetails = new ArrayList<>();

    public static FSchedule from(Member member, FScheduleSaveRequestRequest dto){
        FSchedule fSchedule = new FSchedule();
        fSchedule.member = member;
        fSchedule.title = dto.getTitle();
        fSchedule.commonDescription = dto.getCommonDescription();
        fSchedule.startDate = LocalDateTime.of(dto.getStartDate(),LocalTime.of(0,0));
        fSchedule.endDate = LocalDateTime.of(dto.getEndDate(),LocalTime.of(0,0));
        return fSchedule;
    }

    @Override
    public List<ScheduleAble> getScheduleAbles() {
        return fScheduleDetails.stream().map(fScheduleDetail -> (ScheduleAble) fScheduleDetail).toList();
    }

    public void update(FScheduleUpdateRequest dto){
        super.title = dto.getTitle();
        super.commonDescription = dto.getCommonDescription();
    }
}