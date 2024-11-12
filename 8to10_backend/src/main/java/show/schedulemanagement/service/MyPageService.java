package show.schedulemanagement.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.dto.mypage.ProfileResponse;

@Service
@Transactional(readOnly = true)
public class MyPageService {

    public ProfileResponse getProfile(Member member){
        return ProfileResponse.of(member);
    }
}
