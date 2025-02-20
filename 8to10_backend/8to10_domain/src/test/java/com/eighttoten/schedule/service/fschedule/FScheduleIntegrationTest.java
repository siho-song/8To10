package com.eighttoten.schedule.service.fschedule;

import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("고정일정 통합테스트")
@SpringBootTest
public class FScheduleIntegrationTest {

    //    @Test
//    @DisplayName("고정일정 생성 - 자식 고정일정을 생성한다.")
//    @WithUserDetails(value = "normal@example.com")
//    @Transactional
//    void addDetailsForEachEvent() {
//        //given
//        LocalDateTime startDateTime = LocalDateTime.of(LocalDate.of(2024, 9, 3), LocalTime.of(0, 0));
//        LocalDateTime endDateTime = LocalDateTime.of(LocalDate.of(2024, 9, 22), LocalTime.of(0, 0));
//
//        FScheduleSaveRequest request = FScheduleSaveRequest.builder()
//                .title("테스트 title ")
//                .commonDescription("테스트 commonDescription")
//                .startDateTime(startDateTime)
//                .endDateTime(endDateTime)
//                .days(new ArrayList<>(List.of("mo", "we", "fr")))
//                .frequency("weekly")
//                .startTime(LocalTime.of(8,0))
//                .duration(LocalTime.of(2, 0))
//                .build();
//
//        Member member = memberDetailsService.getAuthenticatedMember();
//
//        //when
//        fScheduleService.save(request.toNewFSchedule(member.getId()));
//
//        //then
//        List<FScheduleDetail> fScheduleDetails = fScheduleDetailRepository.findAllBetweenStartAndEnd(
//                member.getEmail(), startDateTime, endDateTime);
//
//        assertThat(fScheduleDetails.size()).isEqualTo(8);
//    }
}
