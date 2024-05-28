package show.schedulemanagement.domain.schedule.vSchedule;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.ScheduleAble;
import show.schedulemanagement.domain.schedule.Schedule;
import show.schedulemanagement.dto.schedule.request.VariableAddDto;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@DynamicInsert
@DiscriminatorValue(value = "V")
@ToString(callSuper = true)
public class VSchedule extends Schedule implements ScheduleAble {

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean completeStatus;

    public static VSchedule createVSchedule(Member member, VariableAddDto variableRequestDto){
        VSchedule vSchedule = new VSchedule();
        vSchedule.member = member;
        vSchedule.title = variableRequestDto.getTitle();
        vSchedule.commonDescription = variableRequestDto.getCommonDescription();
        vSchedule.startDate = variableRequestDto.getStart();
        vSchedule.endDate = variableRequestDto.getEnd();
        return vSchedule;
    }

    @Override
    public boolean isConflict(ScheduleAble schedule) {
        LocalDateTime start = schedule.getStartDate();
        LocalDateTime end = schedule.getEndDate();
        LocalDateTime scheduleStart = super.startDate;
        LocalDateTime scheduleEnd = super.endDate;
        return end.isAfter(scheduleStart) || start.isBefore(scheduleEnd);
    }

    @Override
    public List<ScheduleAble> getScheduleAbles() {
        return List.of(this);
    }
}