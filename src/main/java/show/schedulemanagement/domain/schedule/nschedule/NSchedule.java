package show.schedulemanagement.domain.schedule.nSchedule;

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
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.Schedule;
import show.schedulemanagement.domain.schedule.ScheduleAble;
import show.schedulemanagement.domain.schedule.fSchedule.FScheduleDetail;
import show.schedulemanagement.dto.schedule.request.NormalAddDto;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@DynamicInsert
@DiscriminatorValue(value = "N")
public class NSchedule extends Schedule{

    @Column(nullable = false)
    @ColumnDefault(value = "'00:00:00'")
    private LocalTime bufferTime;

    private Integer totalValue;

    @OneToMany(mappedBy = "nSchedule", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<NScheduleDetail> nScheduleDetails = new ArrayList<>();

    public static NSchedule createNSchedule(Member member, NormalAddDto normalRequestDto){
        NSchedule nSchedule = new NSchedule();
        nSchedule.member = member;
        nSchedule.title = normalRequestDto.getTitle();
        nSchedule.commonDescription = normalRequestDto.getCommonDescription();
//        nSchedule.startDate = normalRequestDto.getStart();
//        nSchedule.endDate = normalRequestDto.getEnd();
        nSchedule.bufferTime = normalRequestDto.getBufferTime();
        return nSchedule;
    }

    @Override
    public boolean isConflict(ScheduleAble schedule) {
        LocalDateTime start = schedule.getStartDate();
        LocalDateTime end = schedule.getEndDate();
        for (NScheduleDetail nScheduleDetail : nScheduleDetails) {
            if(!end.isAfter(nScheduleDetail.getStartDate()) || start.isBefore(nScheduleDetail.getEndDate())){
                return false;
            }
        }
        return true;
    }

    @Override
    public List<ScheduleAble> getScheduleAbles() {
        return nScheduleDetails.stream().map(nScheduleDetail -> (ScheduleAble) nScheduleDetail).toList();
    }

    //TODO 편의 메서드 구현

}