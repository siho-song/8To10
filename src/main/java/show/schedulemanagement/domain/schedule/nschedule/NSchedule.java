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
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.Schedule;
import show.schedulemanagement.domain.schedule.ScheduleAble;
import show.schedulemanagement.dto.schedule.request.NormalAddDto;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@DynamicInsert
@DiscriminatorValue(value = "N")
@ToString(callSuper = true)
public class NSchedule extends Schedule{
    @Column(nullable = false)
    @ColumnDefault(value = "'00:00:00'")
    private LocalTime bufferTime;

    private Integer totalAmount;

    @OneToMany(mappedBy = "nSchedule", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<NScheduleDetail> nScheduleDetails = new ArrayList<>();

    public static NSchedule createNSchedule(Member member, NormalAddDto normalAddDto){
        NSchedule nSchedule = new NSchedule();
        nSchedule.member = member;
        nSchedule.title = normalAddDto.getTitle();
        nSchedule.commonDescription = normalAddDto.getCommonDescription();
        nSchedule.startDate = LocalDateTime.of(normalAddDto.getStartDate(),LocalTime.of(0,0));
        nSchedule.endDate = LocalDateTime.of(normalAddDto.getEndDate(),LocalTime.of(0,0));
        nSchedule.bufferTime = normalAddDto.getBufferTime();
        nSchedule.totalAmount = normalAddDto.getTotalAmount();
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

    public void minusUpdateTotalAmount(int updateAmount) {
        this.totalAmount -= updateAmount;
    }
}