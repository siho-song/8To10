package show.schedulemanagement.domain.schedule.nschedule;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "ND_VALUE")
public class NDValue {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "nd_value_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "n_schedule_detatil_id")
    private NScheduleDetail nScheduleDetail;

    private Integer value;
}