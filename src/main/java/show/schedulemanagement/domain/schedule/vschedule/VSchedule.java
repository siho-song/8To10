package show.schedulemanagement.domain.schedule.vschedule;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.Schedule;
import show.schedulemanagement.web.request.schedule.VariableRequestDto;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@DynamicInsert
@DiscriminatorValue(value = "V")
public class VSchedule extends Schedule{
    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private String day;

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean completeStatus;

    public static VSchedule createVSchedule(Member member, VariableRequestDto variableRequestDto){
        VSchedule vSchedule = new VSchedule();
        vSchedule.member = member;
        vSchedule.title = variableRequestDto.getTitle();
        vSchedule.description = variableRequestDto.getDescription();
        vSchedule.startDate = variableRequestDto.getStartDate();
        vSchedule.endDate = variableRequestDto.getEndDate();
        vSchedule.startTime = variableRequestDto.getStartTime();
        vSchedule.endTime = variableRequestDto.getEndTime();
        vSchedule.day = variableRequestDto.getDay();
        return vSchedule;
    }
}