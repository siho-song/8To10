package com.eighttoten.service.schedule.fschedule;

import static com.eighttoten.exception.ExceptionCode.NOT_FOUND_F_DETAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.eighttoten.exception.ExceptionCode;
import com.eighttoten.exception.NotFoundEntityException;
import com.eighttoten.member.domain.Member;
import com.eighttoten.schedule.domain.fschedule.FScheduleDetail;
import com.eighttoten.schedule.domain.fschedule.repository.FScheduleDetailRepository;
import com.eighttoten.schedule.dto.request.fschedule.FixDetailUpdateRequest;
import com.eighttoten.schedule.service.fschedule.FScheduleDetailService;
import com.eighttoten.service.MemberDetailsService;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@Transactional
@DisplayName("고정일정 자식일정 서비스 테스트")
class FScheduleDetailServiceTest {
    @Autowired
    FScheduleDetailRepository fScheduleDetailRepository;

    @Autowired
    FScheduleDetailService fScheduleDetailService;

    @Autowired
    MemberDetailsService memberDetailsService;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName(value = "고정일정의 자식일정 단건삭제 정상작동")
    @WithUserDetails(value = "normal@example.com")
    @Transactional
    void deleteById() {
        Member member = memberDetailsService.getAuthenticatedMember();
        Long id = 1L;
        fScheduleDetailService.deleteById(member, id);

        assertThatThrownBy(() -> fScheduleDetailRepository.findById(1L)
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_F_DETAIL)))
                .isInstanceOf(NotFoundEntityException.class);
    }

    @Test
    @DisplayName(value = "특정날짜 이후의 자식일정 모두 삭제 정상작동")
    @Transactional
    void deleteByStartDateGE(){
        Member member = memberDetailsService.getAuthenticatedMember();
        Long parentId = 1L;
        LocalDateTime startDateTimeTime = LocalDateTime.of(2024, 1, 1, 15, 30);

        FScheduleDetail target = fScheduleDetailRepository.findAllWithParentByMemberEmail(member.getEmail()).stream()
                .filter(e -> e.getFScheduleId().equals(parentId) && e.getScheduleStart().isAfter(startDateTimeTime))
                .findFirst()
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_F_DETAIL));

        fScheduleDetailService.deleteByStartDateGEAndMemberAndParentId(startDateTimeTime, member, parentId);
        
        assertThatThrownBy(() -> fScheduleDetailRepository.findById(target.getId())
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_F_DETAIL)))
                .isInstanceOf(NotFoundEntityException.class);
    }

    @Test
    @DisplayName(value = "자식일정 단건 업데이트")
    @Transactional
    void update(){
        Member member = memberDetailsService.getAuthenticatedMember();
        Long id = 1L;
        LocalDateTime startDateTime = LocalDateTime.of(2024, 9, 9, 10, 30);
        LocalDateTime endDateTime = LocalDateTime.of(2024, 9, 9, 12, 30);
        String detailDescription = "수정된 메모";
        FixDetailUpdateRequest request = FixDetailUpdateRequest.builder()
                .id(id)
                .detailDescription(detailDescription)
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .build();

        fScheduleDetailService.update(member, request.toFDetailUpdate());

        FScheduleDetail fScheduleDetail = fScheduleDetailRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_F_DETAIL));

        assertThat(fScheduleDetail.getDetailDescription()).isEqualTo(detailDescription);
        assertThat(fScheduleDetail.getScheduleStart()).isEqualTo(startDateTime);
        assertThat(fScheduleDetail.getScheduleEnd()).isEqualTo(endDateTime);
    }

    @Test
    @DisplayName(value = "자식일정 단건 업데이트 예외발생- 작성자가 불일치")
    void update_not_equal_createdBy(){
        Member member = memberDetailsService.getAuthenticatedMember();
        Long id = 7L;
        LocalDateTime startDateTime = LocalDateTime.of(2024, 9, 9, 10, 30);
        LocalDateTime endDateTime = LocalDateTime.of(2024, 9, 9, 12, 30);
        String detailDescription = "수정된 메모";
        FixDetailUpdateRequest request = FixDetailUpdateRequest.builder()
                .id(id)
                .detailDescription(detailDescription)
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .build();

        assertThatThrownBy(() -> fScheduleDetailService.update(member, request.toFDetailUpdate())).isInstanceOf(
                RuntimeException.class);
    }
}