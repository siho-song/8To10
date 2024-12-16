package com.eighttoten.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.eighttoten.member.domain.Mode;
import com.eighttoten.member.dto.request.SignUpRequest;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import com.eighttoten.member.domain.Gender;
import com.eighttoten.member.domain.Member;
import com.eighttoten.member.domain.repository.MemberRepository;

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
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .username("testUser4")
                .password("1234")
                .email("testUser4@test.com")
                .gender(Gender.FEMALE)
                .phoneNumber("01099920438")
                .nickname("테스트유저4")
                .mode(Mode.MILD)
                .isAuthPhone(false)
                .isAuthEmail(false)
                .build();
        Member member4 = Member.from(signUpRequest);

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
}