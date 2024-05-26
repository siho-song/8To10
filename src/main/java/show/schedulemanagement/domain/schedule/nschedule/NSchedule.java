package show.schedulemanagement.domain.schedule.nSchedule;

import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import show.schedulemanagement.domain.CategoryUnit;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.Schedule;
import show.schedulemanagement.dto.schedule.request.NormalRequestDto;

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

    @OneToMany(mappedBy = "nSchedule", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<NScheduleDetail> nScheduleDetails = new ArrayList<>();

    public static NSchedule createNSchedule(Member member, NormalRequestDto normalRequestDto){
        NSchedule nSchedule = new NSchedule();
        nSchedule.member = member;
        nSchedule.title = normalRequestDto.getTitle();
        nSchedule.description = normalRequestDto.getCommonDescription();
//        nSchedule.startDate = normalRequestDto.getStart();
//        nSchedule.endDate = normalRequestDto.getEnd();
        nSchedule.categoryUnit = normalRequestDto.getCategoryUnit();
        nSchedule.bufferTime = normalRequestDto.getBufferTime();
        nSchedule.frequency = normalRequestDto.getFrequency();
        return nSchedule;
    }

    //TODO 편의 메서드 구현

}