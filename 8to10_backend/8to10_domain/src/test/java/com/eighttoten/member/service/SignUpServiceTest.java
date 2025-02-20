package com.eighttoten.member.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.eighttoten.TestDataUtils;
import com.eighttoten.exception.BadRequestException;
import com.eighttoten.member.domain.Member;
import com.eighttoten.member.domain.MemberRepository;
import com.eighttoten.member.domain.NewMember;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@DisplayName("회원가입 서비스 테스트")
public class SignUpServiceTest {
    @MockBean
    MemberRepository memberRepository;

    @Autowired
    SignUpService signUpService;

    @Test
    @DisplayName("이메일 중복확인 - 중복된 이메일이 아니면 성공한다")
    void not_duplicated_email(){
        //given
        String email = "test@test.com";
        when(memberRepository.findByEmail(any())).thenReturn(Optional.empty());

        //when
        Boolean isDuplicated = signUpService.isDuplicatedEmail(email);

        //then
        assertThat(isDuplicated).isFalse();
    }

    @Test
    @DisplayName("닉네임 중복확인 - 중복된 닉네임이 아니면 성공한다")
    void not_duplicated_nickname(){
        //given
        String nickname = "test";
        when(memberRepository.findByNickname(any())).thenReturn(Optional.empty());

        //when
        Boolean isDuplicated = signUpService.isDuplicatedNickname(nickname);

        //then
        assertThat(isDuplicated).isFalse();
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일이 중복되면 회원가입에 실패한다")
    void duplicated_email(){
        //given
        String email = "test@test.com";
        NewMember newMember = new NewMember(null, null, email, null,
                null, null, null, null, false, false);
        Member member = TestDataUtils.createTestMember(null, email);
        when(memberRepository.findByEmail(any())).thenReturn(Optional.of(member));

        //when,then
        assertThatThrownBy(() -> signUpService.signUp(newMember)).isInstanceOf(BadRequestException.class);
    }

    @Test
    @DisplayName("회원가입 실패 - 닉네임이 중복되면 회원가입에 실패한다")
    void duplicated_nickname(){
        //given
        String nickname = "test";
        NewMember newMember = new NewMember(null, nickname, null, null,
                null, null, null, null, false, false);
        Member member = TestDataUtils.createTestMember(null, null);
        when(memberRepository.findByNickname(any())).thenReturn(Optional.of(member));

        //when,then
        assertThatThrownBy(() -> signUpService.signUp(newMember)).isInstanceOf(BadRequestException.class);
    }
}
