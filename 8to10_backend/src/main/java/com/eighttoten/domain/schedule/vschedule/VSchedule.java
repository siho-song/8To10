package com.eighttoten.domain.schedule.vschedule;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import com.eighttoten.domain.member.Member;
import com.eighttoten.domain.schedule.Schedule;
import com.eighttoten.domain.schedule.ScheduleAble;
import com.eighttoten.dto.schedule.request.vschedule.VScheduleSave;
import com.eighttoten.dto.schedule.request.vschedule.VScheduleUpdate;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@DiscriminatorValue(value = "V")
@ToString(callSuper = true)
public class VSchedule extends Schedule implements ScheduleAble {
    public static VSchedule createVSchedule(Member member, VScheduleSave variableRequestDto){
        VSchedule vSchedule = new VSchedule();
        vSchedule.member = member;
        vSchedule.title = variableRequestDto.getTitle();
        vSchedule.commonDescription = variableRequestDto.getCommonDescription();
        vSchedule.startDate = variableRequestDto.getStart();
        vSchedule.endDate = variableRequestDto.getEnd();
        return vSchedule;
    }

    @Override
    public List<ScheduleAble> getScheduleAbles() {
        return List.of(this);
    }

    public void update(VScheduleUpdate update){
        super.title = update.getTitle();
        super.commonDescription = update.getCommonDescription();
        super.startDate = update.getStartDate();
        super.endDate = update.getEndDate();
    }
}