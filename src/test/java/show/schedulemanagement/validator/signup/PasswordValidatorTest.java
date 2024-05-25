package show.schedulemanagement.validator.signup;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PasswordValidatorTest {
    @Test
    void valid(){
        PasswordValidator validator = new PasswordValidator();

        // 유효한 비밀번호
        System.out.println(validator.isValid("asdf1234!", null)); // true

        // 유효하지 않은 비밀번호들
        System.out.println(validator.isValid("asdf", null)); // false
        System.out.println(validator.isValid("asdf1234", null)); // false
        System.out.println(validator.isValid("1234!", null)); // false
        System.out.println(validator.isValid("asdf!", null)); // false
    }

}