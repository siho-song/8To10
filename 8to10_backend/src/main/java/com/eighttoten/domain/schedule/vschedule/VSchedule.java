package com.eighttoten.domain.schedule.vschedule;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.eighttoten.domain.member.Member;
import com.eighttoten.domain.schedule.Schedule;
import com.eighttoten.domain.schedule.ScheduleAble;
import com.eighttoten.dto.schedule.request.vschedule.VScheduleSave;
import com.eighttoten.dto.schedule.request.vschedule.VScheduleUpdate;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@DiscriminatorValue(value = "V")
@Table(name = "v_schedule")
public class VSchedule extends Schedule implements ScheduleAble {
    public static VSchedule from(Member member, VScheduleSave dto){
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

    public void update(VScheduleUpdate dto){
        super.title = dto.getTitle();
        super.commonDescription = dto.getCommonDescription();
        super.startDate = dto.getStartDate();
        super.endDate = dto.getEndDate();
    }
}