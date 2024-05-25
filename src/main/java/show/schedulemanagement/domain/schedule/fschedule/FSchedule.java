package show.schedulemanagement.domain.schedule.fSchedule;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.Schedule;
import show.schedulemanagement.dto.request.schedule.FixRequestDto;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
@DiscriminatorValue(value = "F")
public class FSchedule extends Schedule{
    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime duration;

    @Column(nullable = false)
    private String frequency;

    public static FSchedule createFSchedule(Member member, FixRequestDto fixRequestDto){
        FSchedule fSchedule = new FSchedule();
        fSchedule.member = member;
        fSchedule.title = fixRequestDto.getTitle();
        fSchedule.description = fixRequestDto.getDescription();
        fSchedule.startDate = fixRequestDto.getStartDate();
        fSchedule.endDate = fixRequestDto.getEndDate();
        fSchedule.startTime = fixRequestDto.getStartTime();
        fSchedule.duration = fixRequestDto.getDuration();
        fSchedule.frequency = fixRequestDto.getFrequency();
        return fSchedule;
    }

}