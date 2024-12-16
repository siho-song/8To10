package com.eighttoten.web.request.signup;

import static org.assertj.core.api.Assertions.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import com.eighttoten.member.dto.request.SignUpRequest;


@SpringBootTest
class SignUpRequestTest {

    Logger logger = LoggerFactory.getLogger(SignUpRequestTest.class);
    private static Validator validator;

    @BeforeAll
    public static void init(){
        validator= Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void 사용자_이름_DTO_NotNull_체크(){
        //given
        SignUpRequest signUp = SignUpRequest.builder()
                .username(null)
                .nickname("asadf")
                .password("asdfg123!!")
                .email("asdf@naver.com")
                .build();
        //when
        Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(signUp);

        //then
        assertThat(violations.size()).isNotEqualTo(0);
    }

    @ParameterizedTest
    @ValueSource(strings = {"두자","세글자","네글자아","다섯글자다","여섯글자아아"})
    public void 사용자_이름_DTO_한글_2자에서_6자_유효성_통과(String username){
        //given
        SignUpRequest signUp = SignUpRequest.builder()
                .username(username)
                .nickname("asadf")
                .password("asdfg123!!")
                .phoneNumber("01012345678")
                .email("asdf@naver.com")
                .build();
        //when
        Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(signUp);
        for (ConstraintViolation<SignUpRequest> violation : violations) {
            System.out.println("violation = " + violation);
        }

        //then
        Assertions.assertThat(violations.size()).isEqualTo(0);
    }

    @ParameterizedTest
    @ValueSource(strings = {"asdf","아!","네글 자아","123"})
    public void 사용자_이름_DTO_유효성_통과_실패(String username){
        //given
        SignUpRequest signUp = SignUpRequest.builder()
                .username(username)
                .nickname("asadf")
                .password("asdfg123!!")
                .phoneNumber("01012345678")
                .email("asdf@naver.com")
                .build();
        //when
        Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(signUp);

        //then
        Assertions.assertThat(violations.size()).isNotEqualTo(0);
    }


}