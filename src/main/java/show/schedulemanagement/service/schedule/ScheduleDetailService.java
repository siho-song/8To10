package show.schedulemanagement.service.schedule;

import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.fSchedule.FScheduleDetail;
import show.schedulemanagement.domain.schedule.nSchedule.NScheduleDetail;

public interface ScheduleDetailService {
    void deleteFdById(Member member, Long id);
    void deleteNdById(Member member, Long id);
    FScheduleDetail findFdById(Long id);
    NScheduleDetail findNdById(Long id);
}
