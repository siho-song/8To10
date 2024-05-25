package show.schedulemanagement.domain.schedule;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import show.schedulemanagement.domain.baseEntity.BaseEntity;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.fSchedule.FSchedule;
import show.schedulemanagement.domain.schedule.nSchedule.NSchedule;
import show.schedulemanagement.domain.schedule.vSchedule.VSchedule;
import show.schedulemanagement.dto.request.schedule.FixRequestDto;
import show.schedulemanagement.dto.request.schedule.NormalRequestDto;
import show.schedulemanagement.dto.request.schedule.ScheduleRequestDto;
import show.schedulemanagement.dto.request.schedule.VariableRequestDto;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype")
@ToString(exclude = {"member"})
public abstract class Schedule extends BaseEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "schedule_id")
    protected Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    protected Member member;

    @Column(nullable = false)
    protected String title;

    @Column(columnDefinition = "TEXT")
    protected String description;

    @Column(nullable = false)
    protected LocalDate startDate;

    @Column(nullable = false)
    protected LocalDate endDate;

    public static Schedule createSchedule(Member member, ScheduleRequestDto scheduleRequestDto) {
        String scheduleType = scheduleRequestDto.getDtype();

        if (scheduleType.equals("F")) {
            FixRequestDto fixRequestDto = (FixRequestDto) scheduleRequestDto;
            return FSchedule.createFSchedule(member, fixRequestDto);
        }
        else if (scheduleType.equals("V")) {
            VariableRequestDto variableRequestDto = (VariableRequestDto) scheduleRequestDto;
            return VSchedule.createVSchedule(member, variableRequestDto);
        }
        NormalRequestDto normalRequestDto = (NormalRequestDto) scheduleRequestDto;

        return NSchedule.createNSchedule(member,normalRequestDto);
    }

}