package show.schedulemanagement.domain.achievement;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import show.schedulemanagement.domain.CategoryUnit;
import show.schedulemanagement.domain.baseEntity.BaseEntity;
import show.schedulemanagement.domain.member.Member;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@DynamicInsert
public class CatUnitAch extends BaseEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "cat_unit_ach_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    @ColumnDefault(value = "NONE")
    private CategoryUnit categoryUnit;

    @Column(nullable = false)
    private Double achievementRate;

}