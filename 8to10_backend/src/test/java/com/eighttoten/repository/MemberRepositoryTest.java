package com.eighttoten.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import com.eighttoten.domain.member.Gender;
import com.eighttoten.domain.member.Member;
import com.eighttoten.repository.member.MemberRepository;

@SpringBootTest
@Transactional
@Rollback
class MemberRepositoryTest {

    @Autowired
    public MemberRepository memberRepository;

    @Autowired
    public EntityManager em;

    @Test
    @DisplayName(value = "Member CRUD 테스트")
    void crud(){
        /*
        save, read
         */
        //given
        Member member4 = Member.builder()
                .username("testUser4")
                .password("1234")
                .email("testUser4@test.com")
                .gender(Gender.FEMALE)
                .phoneNumber("01099920438")
                .nickname("테스트유저4")
                .authPhone(false)
                .authEmail(false)
                .build();

        //when
        Member saveMember = memberRepository.save(member4);
        Member findMember = memberRepository.findById(saveMember.getId()).orElse(null);

        //then
        assertThat(findMember).isNotNull();
        assertThat(saveMember).isEqualTo(findMember);

        /*
        update test
         */
        saveMember.changeNickname("testUser444");
        assertThat(saveMember.getNickname()).isEqualTo("testUser444");

        /*
        delete
         */
        memberRepository.delete(member4);
        assertThatThrownBy(()-> memberRepository.findById(member4.getId()).orElseThrow(NullPointerException::new))
                .isInstanceOf(NullPointerException.class);
    }

//    @Test
//    @DisplayName(value = "멤버객체의 ROLE의 기본값은 ROLE.NORMAL_USER 이다.")
//    void 멤버객체_ROLE의_기본_값_NORMAL_USER(){
//        //given
//        Member member4 = Member.builder()
//                .username("testUser4")
//                .password("1234")
//                .email("testUser4@test.com")
//                .gender(Gender.FEMALE)
//                .nickname("테스트유저4")
//                .build();
//
//        Member saveMember = memberRepository.save(member4);
//        em.flush();
//        em.clear();
//
//        //when
//        Member findMember = memberRepository.findById(saveMember.getId()).orElse(null);
//
//        //then
//        assert findMember != null;
//        assertThat(findMember.getRole()).isEqualTo(Role.NORMAL_USER);
//    }

//    @Test
//    @DisplayName("이메일로 Member를 조회하면서 roles를 페치 조인")
//    void findWithRolesByEmail() {
//        String email = "test@example.com";
//
//        Optional<Member> memberOpt = memberRepository.findWithRolesByEmail(email);
//        assertThat(memberOpt).isPresent();
//
//        Member member = memberOpt.get();
//        assertThat(member.getRoles()).isNotNull();
//        assertThat(member.getRoles()).isNotEmpty();
//    }
}