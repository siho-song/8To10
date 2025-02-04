package com.eighttoten;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.eighttoten.exception.ExceptionCode;
import com.eighttoten.exception.NotFoundEntityException;
import com.eighttoten.member.MemberEntity;
import com.eighttoten.member.domain.Gender;
import com.eighttoten.member.domain.Mode;
import com.eighttoten.member.domain.Member;
import com.eighttoten.member.domain.NewMember;
import com.eighttoten.member.domain.Role;
import com.eighttoten.member.repository.MemberJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class MemberEntityJpaRepositoryTest {
    @Autowired
    private MemberJpaRepository memberJpaRepository;

    private Member createTestMember(Long id) {
        Member member = new Member(
                id,
                "testUser4",
                "테스트유저4",
                "testUser4@test.com",
                "1234",
                "01099920438",
                Gender.FEMALE,
                Role.NORMAL_USER,
                Mode.MILD,
                null,
                0,
                false,
                false
        );

        return member;
    }

    @Test
    @DisplayName("MemberEntity 저장 테스트")
    void saveTest() {
        // given
        NewMember newMember = new NewMember("test", "test", "test", "test", "test", Gender.MALE, Role.NORMAL_USER,
                Mode.MILD, false, false);
        MemberEntity memberEntity = MemberEntity.from(newMember);

        // when
        MemberEntity savedMemberEntity = memberJpaRepository.save(memberEntity);
        MemberEntity findMemberEntity = memberJpaRepository.findById(savedMemberEntity.getId()).orElse(null);

        // then
        assertThat(findMemberEntity).isNotNull();
        assertThat(savedMemberEntity.getId()).isEqualTo(findMemberEntity.getId());

        memberJpaRepository.deleteById(savedMemberEntity.getId());
    }

    @Test
    @DisplayName("MemberEntity 수정 테스트")
    void updateTest() {
        // given
        MemberEntity memberEntity = createTestMember();
        MemberEntity savedMemberEntity = memberJpaRepository.save(memberEntity);

        // when
        memberJpaRepository.findById(1L).orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_MEMBER));
        savedMemberEntity.update("testUserUpdated");

        // then
        assertThat(savedMemberEntity.getNickname()).isEqualTo("testUserUpdated");
    }

    @Test
    @DisplayName("MemberEntity 삭제 테스트")
    @Transactional
    void deleteTest() {
        // given
        MemberEntity memberEntity = createTestMember();
        MemberEntity savedMemberEntity = memberJpaRepository.save(memberEntity);

        // when
        memberJpaRepository.delete(savedMemberEntity);

        // then
        assertThatThrownBy(() -> memberJpaRepository.findById(savedMemberEntity.getId()).orElseThrow(
                () -> new IllegalArgumentException("MemberEntity not found")
        )).isInstanceOf(IllegalArgumentException.class);
    }
}