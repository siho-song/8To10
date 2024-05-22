package show.schedulemanagement.domain.schedule.nschedule;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
@Builder
@AllArgsConstructor
public class NRange {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "n_range_id")
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "n_schedule_id", nullable = false)
    private NSchedule nSchedule;

    @Column(nullable = false)
    private Integer startRange;

    @Column(nullable = false)
    private Integer endRange;
}





