package show.schedulemanagement.repository.schedule;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.CategoryUnit;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.Schedule;
import show.schedulemanagement.repository.member.MemberRepository;
import show.schedulemanagement.dto.request.schedule.FixRequestDto;
import show.schedulemanagement.dto.request.schedule.NormalRequestDto;
import show.schedulemanagement.dto.request.schedule.ScheduleRequestDto;
import show.schedulemanagement.dto.request.schedule.VariableRequestDto;

@SpringBootTest
@Transactional
@Rollback
class ScheduleRepositoryTest {

    @Autowired
    public MemberRepository memberRepository;
    @Autowired
    public ScheduleRepository scheduleRepository;
    @Autowired
    public EntityManager em;

//    initMember
//    (username, nickname, email, password, gender, mode, image_file, role, created_at, created_by, updated_at, updated_by, score)
//    ('user1', 'nick1', 'user1@example.com', 'password1', 'MALE', 'MILD', NULL, 'NORMAL_USER', NOW(), 'system', NOW(), 'system', 10),
//    ('user2', 'nick2', 'user2@example.com', 'password2', 'FEMALE', 'SPICY', NULL, 'ADMIN', NOW(), 'system', NOW(), 'system', 20),
//    ('user3', 'nick3', 'user3@example.com', 'password3', 'MALE', 'MILD', NULL, 'PUNCTUAL_USER', NOW(), 'system', NOW(), 'system', 30);

    static Stream<ScheduleRequestDto> provideScheduleRequestDtos() {
        return Stream.of(getFixRequestDto(), getVariableRequestDto(), getNormalRequestDto());
    }

    @ParameterizedTest
    @MethodSource(value = "provideScheduleRequestDtos")
    void crud(ScheduleRequestDto scheduleRequestDto){
        //save
        Member member = memberRepository.findByEmail("user1@example.com").orElse(null);
        assert member != null;

        Schedule schedule = Schedule.createSchedule(member,scheduleRequestDto);

        assertThat(scheduleRepository.save(schedule).getTitle()).isEqualTo(scheduleRequestDto.getTitle());
        assertThat(scheduleRepository.findAll().size()).isEqualTo(10);
        assertThat(scheduleRepository.findByMemberId(member.getId()).size()).isEqualTo(4);
    }

    private static NormalRequestDto getNormalRequestDto() {
        return new NormalRequestDto("Normal Schedule", "Description", LocalDate.now(), LocalDate.now().plusDays(14),
                "N", CategoryUnit.PAGE, LocalTime.of(1, 0), "weekly", List.of("mo", "we", "fr"), 100, 300, null);
    }

    private static VariableRequestDto getVariableRequestDto() {
        return new VariableRequestDto("Variable Schedule", "Description", LocalDate.now(), LocalDate.now(), "V",
                LocalTime.now().plusHours(2), LocalTime.now().plusHours(4), "tu");
    }

    private static FixRequestDto getFixRequestDto() {
        return new FixRequestDto("Fixed Schedule", "Description", LocalDate.now(), LocalDate.now().plusDays(7), "F",
                LocalTime.now(), LocalTime.of(2, 0, 0), "weekly", List.of("mo", "th"));
    }

}