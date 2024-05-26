package show.schedulemanagement.domain.schedule.vSchedule;

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
import show.schedulemanagement.dto.schedule.request.VariableRequestDto;

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
    @ColumnDefault(value = "false")
    private boolean completeStatus;


    public static VSchedule createVSchedule(Member member, VariableRequestDto variableRequestDto){
        VSchedule vSchedule = new VSchedule();
        vSchedule.member = member;
        vSchedule.title = variableRequestDto.getTitle();
        vSchedule.description = variableRequestDto.getCommonDescription();
        vSchedule.startDate = variableRequestDto.getStart().toLocalDate();
        vSchedule.endDate = variableRequestDto.getEnd().toLocalDate();
        vSchedule.startTime = variableRequestDto.getStart().toLocalTime();
        vSchedule.endTime = variableRequestDto.getEnd().toLocalTime();
        return vSchedule;
    }

}