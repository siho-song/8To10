package show.schedulemanagement.repository.schedule;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.Schedule;
import show.schedulemanagement.domain.schedule.fSchedule.QFSchedule;
import show.schedulemanagement.domain.schedule.fSchedule.QFScheduleDetail;
import show.schedulemanagement.domain.schedule.vSchedule.QVSchedule;

@RequiredArgsConstructor
public class ScheduleRepositoryCustomImpl implements ScheduleRepositoryCustom{

    private final EntityManager em;
    private JPAQueryFactory query;

    @Override
    public List<Schedule> findAll() {
        return List.of();
    }

    @Override
    public List<Schedule> findAllBetweenStartAndEnd(
            Member member,
            LocalDateTime start,
            LocalDateTime end) {

        query = new JPAQueryFactory(em);

        QFSchedule qFSchedule = QFSchedule.fSchedule;
        QFScheduleDetail qfScheduleDetail = QFScheduleDetail.fScheduleDetail;
//        QNSchedule qNSchedule = QNSchedule.nSchedule;
        QVSchedule qVSchedule = QVSchedule.vSchedule;

        List<Schedule> schedules = new ArrayList<>();

        //변동일정 불러오기
        schedules.addAll(
                query
                        .select(qVSchedule)
                        .from(qVSchedule)
                        .where(qVSchedule.member.eq(member)
                                .and(qVSchedule.startDate.between(start, end))
                                .and(qVSchedule.endDate.between(start, end)))
                        .fetch());

        //고정일정 불러오기
        schedules.addAll(
                query
                        .select(qFSchedule)
                        .distinct()
                        .from(qFSchedule)
                        .leftJoin(qFSchedule.fScheduleDetails, qfScheduleDetail)
                        .fetchJoin()
                        .where(qFSchedule.member.eq(member)
                                .and(qFSchedule.startDate.between(start, end))
                                .and(qFSchedule.endDate.between(start, end)))
                        .fetch());

        return schedules;
    }
}
