package show.schedulemanagement.domain.achievement;

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
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import show.schedulemanagement.domain.baseEntity.BaseEntity;
import show.schedulemanagement.domain.member.Member;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
@Builder
@AllArgsConstructor
@Table(name = "ACHIEVEMENT")
public class Achievement extends BaseEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "achievement_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id" , nullable = false)
    private Member member;

    @Column(nullable = false)
    private LocalDate achievementDate;

    @Column(nullable = false)
    private Double achievementRate;
}