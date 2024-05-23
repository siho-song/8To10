package show.schedulemanagement.domain.schedule.nSchedule;

import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import java.time.LocalTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import show.schedulemanagement.domain.CategoryUnit;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.Schedule;
import show.schedulemanagement.web.request.schedule.NormalRequestDto;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@DynamicInsert
@DiscriminatorValue(value = "N")
public class NSchedule extends Schedule{
    @Enumerated(value = STRING)
    @Column(nullable = false)
    @ColumnDefault(value = "'NONE'")
    private CategoryUnit categoryUnit;

    @Column(nullable = false)
    @ColumnDefault(value = "'00:00:00'")
    private LocalTime bufferTime;

    @Column(nullable = false)
    private String frequency; //매일 , 매주 , 매월


    public static NSchedule createNSchedule(Member member, NormalRequestDto normalRequestDto){
        NSchedule nSchedule = new NSchedule();
        nSchedule.member = member;
        nSchedule.title = normalRequestDto.getTitle();
        nSchedule.description = normalRequestDto.getDescription();
        nSchedule.startDate = normalRequestDto.getStartDate();
        nSchedule.endDate = normalRequestDto.getEndDate();
        nSchedule.categoryUnit = normalRequestDto.getCategoryUnit();
        nSchedule.bufferTime = normalRequestDto.getBufferTime();
        nSchedule.frequency = normalRequestDto.getFrequency();
        return nSchedule;
    }

}