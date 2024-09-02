package show.schedulemanagement.domain.schedule.fSchedule;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.Schedule;
import show.schedulemanagement.domain.schedule.ScheduleAble;
import show.schedulemanagement.dto.schedule.request.FixAddDto;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
@DiscriminatorValue(value = "F")
@ToString(callSuper = true)
public class FSchedule extends Schedule{

    @OneToMany(mappedBy = "fSchedule" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<FScheduleDetail> fScheduleDetails = new ArrayList<>();

    public static FSchedule createFSchedule(Member member, FixAddDto fixRequestDto){
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
}