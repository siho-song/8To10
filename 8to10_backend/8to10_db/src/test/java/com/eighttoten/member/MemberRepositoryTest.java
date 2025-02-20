package com.eighttoten.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.eighttoten.member.domain.Gender;
import com.eighttoten.member.domain.Member;
import com.eighttoten.member.domain.MemberRepository;
import com.eighttoten.member.domain.Mode;
import com.eighttoten.member.domain.NewMember;
import com.eighttoten.member.domain.Role;
import com.eighttoten.member.repository.MemberRepositoryImpl;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@DisplayName("멤버 레포지토리 테스트")
@Import(MemberRepositoryImpl.class)
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("새로운 멤버 저장에 성공한다.")
    void save() {
        // given
        NewMember newMember = new NewMember("test", "test", "test", "test", "test", Gender.MALE, Role.NORMAL_USER,
                Mode.MILD, false, false);

        // when,then
        assertThatCode(()->memberRepository.save(newMember)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("닉네임, 비밀번호, 새로운 이미지파일 경로 수정에 성공한다.")
    void update() {
        // given
        List<Member> all = memberRepository.findAll();
        Member member = memberRepository.findById(1L).orElseThrow(RuntimeException::new);
        String newNickname = "newNickname";
        String newPassword = "newPassword";
        String newProfileImagePath = "newImage";

        // when
        member.updateNickname(newNickname);
        member.updatePassword(newPassword);
        member.updateProfilePhoto(newProfileImagePath);
        memberRepository.update(member);

        // then
        Member updatedMember = memberRepository.findById(1L).orElseThrow();
        assertThat(updatedMember.getNickname()).isEqualTo(newNickname);
        assertThat(updatedMember.getPassword()).isEqualTo(newPassword);
        assertThat(updatedMember.getProfileImagePath()).isEqualTo(newProfileImagePath);
    }

    @Test
    @DisplayName("id로 멤버를 삭제한다.")
    @Transactional
    void deleteById(){
        //given
        Long id = 1L;

        //when,then
        assertThatCode(()->memberRepository.deleteById(id)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("이메일로 멤버를 조회한다.")
    void findByEmail() {
        // given
        String email = "normal@example.com";

        // when
        Member member = memberRepository.findByEmail(email).orElse(null);

        // then
        assertThat(member).isNotNull();
    }

    @Test
    @DisplayName("닉네임으로 멤버를 조회한다.")
     void findByNickname() {
        // given
        String nickname = "nick1";

        // when
        Member member = memberRepository.findByNickname(nickname).orElse(null);

        // then
        assertThat(member).isNotNull();
    }

    @Test
    @DisplayName("모든 멤버를 조회한다.")
    void findAll() {
        // when
        List<Member> all = memberRepository.findAll();

        // then
        assertThat(all.size()).isNotZero();
    }
}