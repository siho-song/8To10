package com.eighttoten.schedule.service.nschedule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.eighttoten.achievement.Achievement;
import com.eighttoten.achievement.AchievementRepository;
import com.eighttoten.exception.BadRequestException;
import com.eighttoten.member.domain.Member;
import com.eighttoten.schedule.domain.ProgressUpdates;
import com.eighttoten.schedule.domain.ProgressUpdates.ProgressUpdate;
import com.eighttoten.schedule.domain.nschedule.NScheduleCreateInfo;
import com.eighttoten.schedule.domain.nschedule.NScheduleDetail;
import com.eighttoten.schedule.domain.nschedule.NewNSchedule;
import com.eighttoten.schedule.domain.nschedule.repository.NScheduleDetailRepository;
import com.eighttoten.schedule.domain.nschedule.repository.NScheduleRepository;
import com.eighttoten.support.AuthAccessor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;

@DisplayName("일반일정 통합테스트")
@SpringBootTest
public class NScheduleIntegrationTest {
    @Autowired
    AuthAccessor authAccessor;

    @Autowired
    NScheduleService nScheduleService;

    @Autowired
    NScheduleDetailService nScheduleDetailService;

    @Autowired
    NScheduleRepository nScheduleRepository;

    @Autowired
    NScheduleDetailRepository nScheduleDetailRepository;

    @Autowired
    AchievementRepository achievementRepository;

    @Test
    @WithUserDetails(value = "normal@example.com")
    @DisplayName("일반일정 생성 - 일반일정과, 자식 일반일정을 생성한다.")
    void create(){
        Member member = authAccessor.getAuthenticatedMember();
        String title = "테스트용 일반일정";
        String commonDescription = "테스트용 일반일정 메모";
        LocalDateTime startDateTime = LocalDateTime.of(2020, 1, 1,0,0);
        LocalDateTime endDateTime = LocalDateTime.of(2020, 2, 1,0,0);
        LocalTime bufferTime = LocalTime.of(2, 0);
        LocalTime performInDay = LocalTime.of(3, 0);

        NewNSchedule newNSchedule = new NewNSchedule(member.getId(), title, commonDescription,
                startDateTime, endDateTime, bufferTime, 200);

        NScheduleCreateInfo nScheduleCreateInfo = new NScheduleCreateInfo(bufferTime, performInDay, true, true, 7);

        assertThatCode(()->nScheduleService.saveWithDetails(member,nScheduleCreateInfo,newNSchedule)).doesNotThrowAnyException();
    }

    @Test
    @WithUserDetails(value = "normal@example.com")
    @DisplayName("일반일정 생성 - 일과시간보다 더 많은 수행시간으로 생성시 예외발생")
    void create_throw_exception_if_performTime_is_over(){
        Member member = authAccessor.getAuthenticatedMember();
        String title = "테스트용 일반일정";
        String commonDescription = "테스트용 일반일정 메모";
        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = LocalDateTime.of(LocalDate.now().plusMonths(1), LocalTime.of(0, 0));
        LocalTime bufferTime = LocalTime.of(2, 0);
        LocalTime performInDay = LocalTime.of(22, 0);

        NewNSchedule newNSchedule = new NewNSchedule(member.getId(), title, commonDescription,
                startDateTime, endDateTime, bufferTime, 200);

        NScheduleCreateInfo nScheduleCreateInfo = new NScheduleCreateInfo(bufferTime, performInDay, true, true, 5);

        assertThatThrownBy(() -> nScheduleService.saveWithDetails(member, nScheduleCreateInfo,newNSchedule)).isInstanceOf(BadRequestException.class);
    }

    @Test
    @DisplayName("일반일정 자식일정 단건 삭제 - 삭제시 부모일정의 성취총량이 조절된다.")
    @WithUserDetails(value = "normal@example.com")
    void deleteById(){
        //given
        Member member = authAccessor.getAuthenticatedMember();
        long nDetailId = 1L;
        NScheduleDetail nScheduleDetail = nScheduleDetailRepository.findById(nDetailId).orElseThrow();

        //when
        nScheduleDetailService.deleteById(member, nDetailId);

        //then
        assertThat(nScheduleDetailRepository.findById(nDetailId)).isEmpty();

        assertThat(nScheduleRepository.findById(nScheduleDetail.getNScheduleId()).orElseThrow()
                .getTotalAmount()).isEqualTo(80);
    }

    @Test
    @WithUserDetails(value = "normal@example.com")
    @DisplayName("수행량을 업데이트 하려는 일반 자식일정의 날짜와, 업데이트 하려는 날짜가 일치하지 않으면 예외가 발생한다.")
    void updateProgress_not_equal_date() {
        //given
        Member member = authAccessor.getAuthenticatedMember();

        LocalDate date = LocalDate.of(2024, 5, 1);
        Long nDetailId = 2L;
        List<ProgressUpdate> progressUpdates = List.of(new ProgressUpdate(nDetailId, false, 10));
        ProgressUpdates updates = new ProgressUpdates(date, progressUpdates);

        //when
        assertThatThrownBy(() -> nScheduleDetailService.updateProgressList(member, updates)).isInstanceOf(
                BadRequestException.class);
    }

    @Test
    @WithUserDetails(value = "normal@example.com")
    @DisplayName("유저가 수행한 양 만큼 해당 일반 자식일정의 수행양을 업데이트한다.")
    void updateProgress(){
        //given
        Member member = authAccessor.getAuthenticatedMember();

        LocalDate date = LocalDate.of(2024, 5, 2);
        Long nDetailId = 2L;
        List<ProgressUpdate> progressUpdates = List.of(new ProgressUpdate(nDetailId, false, 10));
        ProgressUpdates updates = new ProgressUpdates(date, progressUpdates);

        //when
        nScheduleDetailService.updateProgressList(member, updates);

        //then
        Achievement achievement = achievementRepository.findByMemberIdAndDate(member.getId(), date).orElseThrow();
        NScheduleDetail nScheduleDetail = nScheduleDetailRepository.findById(nDetailId).orElseThrow();
        assertThat(nScheduleDetail.isCompleteStatus()).isFalse();
        assertThat(nScheduleDetail.getAchievedAmount()).isEqualTo(10);
        assertThat(achievement.getAchievementRate()).isEqualTo(0.25);
    }

    @Test
    @WithUserDetails(value = "normal@example.com")
    @DisplayName("유저가 각 일정을 수행한 양 만큼 해당 일반 자식일정의 수행양을 다중 업데이트한다.")
    void updateProgress_multiple(){
        //given
        Member member = authAccessor.getAuthenticatedMember();
        LocalDate date = LocalDate.of(2024, 5, 3);
        Long nDetailId1 = 3L;
        Long nDetailId2 = 8L;
        List<ProgressUpdate> progressUpdates = List.of(
                new ProgressUpdate(nDetailId1, false, 10),
                new ProgressUpdate(nDetailId2, false, 10));

        ProgressUpdates updates = new ProgressUpdates(date, progressUpdates);

        //when
        nScheduleDetailService.updateProgressList(member, updates);

        //then
        Achievement achievement = achievementRepository.findByMemberIdAndDate(member.getId(), date).orElseThrow();
        NScheduleDetail nScheduleDetail1 = nScheduleDetailRepository.findById(nDetailId1).orElseThrow();
        NScheduleDetail nScheduleDetail2 = nScheduleDetailRepository.findById(nDetailId2).orElseThrow();

        assertThat(nScheduleDetail1.isCompleteStatus()).isFalse();
        assertThat(nScheduleDetail1.getAchievedAmount()).isEqualTo(10);
        assertThat(nScheduleDetail2.isCompleteStatus()).isFalse();
        assertThat(nScheduleDetail2.getAchievedAmount()).isEqualTo(10);
        assertThat(achievement.getAchievementRate()).isEqualTo(0.5);
    }

    @Test
    @WithUserDetails(value = "normal@example.com")
    @DisplayName("수행한 양과 일간 총 수행량이 같은 경우 완료상태를 업데이트한다.")
    void updateProgress_totalAmount_equal_achievedAmount(){
        //given
        Member member = authAccessor.getAuthenticatedMember();
        LocalDate date = LocalDate.of(2024, 5, 4);
        Long nDetailId = 4L;

        List<ProgressUpdate> progressUpdates = List.of(new ProgressUpdate(nDetailId, true, 20));
        ProgressUpdates updates = new ProgressUpdates(date, progressUpdates);

        //when
        nScheduleDetailService.updateProgressList(member, updates);

        //then
        NScheduleDetail nScheduleDetail = nScheduleDetailRepository.findById(nDetailId).orElseThrow();
        assertThat(nScheduleDetail.isCompleteStatus()).isTrue();
        assertThat(nScheduleDetail.getAchievedAmount()).isEqualTo(20);
    }

    @Test
    @WithUserDetails(value = "normal@example.com")
    @DisplayName("해당 일정의 수행해야할 양이 0일 경우 완료상태를 업데이트한다.")
    void updateProgress_totalAmount_is_zero(){
        //given
        Member member = authAccessor.getAuthenticatedMember();
        LocalDate date = LocalDate.of(2024, 5, 5);
        Long nDetailId = 5L;

        List<ProgressUpdate> progressUpdates = List.of(new ProgressUpdate(nDetailId, true, 0));
        ProgressUpdates updates = new ProgressUpdates(date, progressUpdates);

        //when
        nScheduleDetailService.updateProgressList(member, updates);

        //then
        NScheduleDetail nScheduleDetail = nScheduleDetailRepository.findById(nDetailId).orElseThrow();
        assertThat(nScheduleDetail.isCompleteStatus()).isTrue();
    }

    @Test
    @WithUserDetails(value = "normal@example.com")
    @DisplayName("유효하지 않은 성취량으로 업데이트를 수행 할 경우 예외가 발생한다.")
    void updateProgress_inValid_achievementAmount(){
        //given
        Member member = authAccessor.getAuthenticatedMember();
        LocalDate date = LocalDate.of(2024, 5, 2);
        Long nDetailId = 2L;

        List<ProgressUpdate> progressUpdates = List.of(new ProgressUpdate(nDetailId, true, 21));
        ProgressUpdates updates = new ProgressUpdates(date, progressUpdates);

        //when,then
        assertThatThrownBy(() -> nScheduleDetailService.updateProgressList(member, updates)).isInstanceOf(
                BadRequestException.class);
    }
}
