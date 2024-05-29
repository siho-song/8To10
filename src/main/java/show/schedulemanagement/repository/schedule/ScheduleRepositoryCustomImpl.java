package show.schedulemanagement.repository.schedule;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.Schedule;
import show.schedulemanagement.domain.schedule.fSchedule.FSchedule;
import show.schedulemanagement.domain.schedule.fSchedule.QFSchedule;
import show.schedulemanagement.domain.schedule.fSchedule.QFScheduleDetail;
import show.schedulemanagement.domain.schedule.nSchedule.NSchedule;
import show.schedulemanagement.domain.schedule.nSchedule.QNSchedule;
import show.schedulemanagement.domain.schedule.nSchedule.QNScheduleDetail;
import show.schedulemanagement.domain.schedule.vSchedule.QVSchedule;
import show.schedulemanagement.domain.schedule.vSchedule.VSchedule;


@Slf4j
public class ScheduleRepositoryCustomImpl implements ScheduleRepositoryCustom{

    private final EntityManager em;

    private JPAQueryFactory query;
    private QFSchedule qFSchedule;
    private QFScheduleDetail qfScheduleDetail;
    private QNSchedule qNSchedule;
    private QNScheduleDetail qnScheduleDetail;
    private QVSchedule qVSchedule;

    public ScheduleRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        init();
    }

    @Override
    public List<Schedule> findAll(Member member) {
        List<Schedule> schedules = new ArrayList<>();

        //변동일정 불러오기
        List<VSchedule> vSchedules = query
                .select(qVSchedule)
                .from(qVSchedule)
                .where(qVSchedule.member.eq(member))
                .fetch();

        schedules.addAll(vSchedules);

        //고정일정 불러오기
        List<FSchedule> fSchedules = query
                .select(qFSchedule)
                .distinct()
                .from(qFSchedule)
                .leftJoin(qFSchedule.fScheduleDetails, qfScheduleDetail)
                .fetchJoin()
                .where(qFSchedule.member.eq(member))
                .fetch();
        schedules.addAll(fSchedules);

        //일반일정 불러오기
        List<NSchedule> nSchedules = query
                .select(qNSchedule)
                .distinct()
                .from(qNSchedule)
                .leftJoin(qNSchedule.nScheduleDetails, qnScheduleDetail)
                .fetchJoin()
                .where(qNSchedule.member.eq(member))
                .fetch();
        schedules.addAll(nSchedules);
        return schedules;
    }

    @Override
    public List<Schedule> findAllBetweenStartAndEnd(
            Member member,
            LocalDateTime start,
            LocalDateTime end) {

        List<Schedule> schedules = new ArrayList<>();

        //변동일정 불러오기
        List<VSchedule> vSchedules = query
                .select(qVSchedule)
                .from(qVSchedule)
                .where(qVSchedule.member.eq(member)
                        .and(qVSchedule.startDate.between(start, end))
                        .and(qVSchedule.endDate.between(start, end)))
                .fetch();

        schedules.addAll(vSchedules);

        //고정일정 불러오기
        List<FSchedule> fSchedules = query
                .select(qFSchedule)
                .distinct()
                .from(qFSchedule)
                .leftJoin(qFSchedule.fScheduleDetails, qfScheduleDetail)
                .fetchJoin()
                .where(qFSchedule.member.eq(member)
                        .and(qFSchedule.startDate.between(start, end))
                        .and(qFSchedule.endDate.between(start, end)))
                .fetch();
        schedules.addAll(fSchedules);

        //일반일정 불러오기
        List<NSchedule> nSchedules = query
                .select(qNSchedule)
                .distinct()
                .from(qNSchedule)
                .leftJoin(qNSchedule.nScheduleDetails, qnScheduleDetail)
                .fetchJoin()
                .where(qNSchedule.member.eq(member)
                        .and(qNSchedule.startDate.between(start, end))
                        .and(qNSchedule.endDate.between(start, end)))
                .fetch();
        schedules.addAll(nSchedules);
        return schedules;
    }

    private void init(){
        query = new JPAQueryFactory(em);
        qFSchedule = QFSchedule.fSchedule;
        qfScheduleDetail = QFScheduleDetail.fScheduleDetail;
        qNSchedule = QNSchedule.nSchedule;
        qnScheduleDetail = QNScheduleDetail.nScheduleDetail;
        qVSchedule = QVSchedule.vSchedule;
    }
}
