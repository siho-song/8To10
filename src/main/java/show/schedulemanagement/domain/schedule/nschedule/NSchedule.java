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
import show.schedulemanagement.dto.schedule.request.nSchedule.NScheduleSave;

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

    public static NSchedule createNSchedule(Member member, NScheduleSave nScheduleSave){
        NSchedule nSchedule = new NSchedule();
        nSchedule.member = member;
        nSchedule.title = nScheduleSave.getTitle();
        nSchedule.commonDescription = nScheduleSave.getCommonDescription();
        nSchedule.startDate = LocalDateTime.of(nScheduleSave.getStartDate(),LocalTime.of(0,0));
        nSchedule.endDate = LocalDateTime.of(nScheduleSave.getEndDate(),LocalTime.of(0,0));
        nSchedule.bufferTime = nScheduleSave.getBufferTime();
        nSchedule.totalAmount = nScheduleSave.getTotalAmount();
        return nSchedule;
    }

    @Override
    public List<ScheduleAble> getScheduleAbles() {
        return nScheduleDetails.stream().map(nScheduleDetail -> (ScheduleAble) nScheduleDetail).toList();
    }

    public void minusUpdateTotalAmount(int updateAmount) {
        this.totalAmount -= updateAmount;
    }
}