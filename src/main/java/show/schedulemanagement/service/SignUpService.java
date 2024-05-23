package show.schedulemanagement.service;

import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.web.request.signup.SignUpRequestDto;

public interface SignUpService {
    Member signUp(SignUpRequestDto signUpRequestDto);
    Boolean isDuplicatedEmail(String email);
    Boolean isDuplicatedNickname(String nickname);
}
