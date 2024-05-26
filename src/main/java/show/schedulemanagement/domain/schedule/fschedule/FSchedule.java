package show.schedulemanagement.domain.schedule.fSchedule;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.Schedule;
import show.schedulemanagement.dto.schedule.request.FixRequestDto;

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

    @OneToMany(mappedBy = "fSchedule" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<FScheduleDetail> fScheduleDetails = new ArrayList<>();

    public static FSchedule createFSchedule(Member member, FixRequestDto fixRequestDto){
        FSchedule fSchedule = new FSchedule();
        fSchedule.member = member;
        fSchedule.title = fixRequestDto.getTitle();
        fSchedule.description = fixRequestDto.getCommonDescription();
//        fSchedule.startDate = fixRequestDto.getStart();
//        fSchedule.endDate = fixRequestDto.getEnd();
        fSchedule.startTime = fixRequestDto.getStartTime();
        fSchedule.duration = fixRequestDto.getDuration();
        fSchedule.frequency = fixRequestDto.getFrequency();
        return fSchedule;
    }

}