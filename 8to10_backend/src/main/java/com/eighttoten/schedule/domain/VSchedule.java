package com.eighttoten.schedule.domain;

import static lombok.AccessLevel.PROTECTED;

import com.eighttoten.member.domain.Member;
import com.eighttoten.schedule.dto.request.VScheduleSaveRequestRequest;
import com.eighttoten.schedule.dto.request.VScheduleUpdateRequest;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@DiscriminatorValue(value = "V")
@Table(name = "v_schedule")
public class VSchedule extends Schedule implements ScheduleAble {
    public static VSchedule from(Member member, VScheduleSaveRequestRequest dto){
        VSchedule vSchedule = new VSchedule();
        vSchedule.member = member;
        vSchedule.title = dto.getTitle();
        vSchedule.commonDescription = dto.getCommonDescription();
        vSchedule.startDate = dto.getStart();
        vSchedule.endDate = dto.getEnd();
        return vSchedule;
    }

    @Override
    public List<ScheduleAble> getScheduleAbles() {
        return List.of(this);
    }

    public void update(VScheduleUpdateRequest dto){
        super.title = dto.getTitle();
        super.commonDescription = dto.getCommonDescription();
        super.startDate = dto.getStartDate();
        super.endDate = dto.getEndDate();
    }
}