package show.schedulemanagement.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.dto.mypage.ProfileResponse;

@SpringBootTest
@DisplayName("마이페이지 서비스 테스트")
class MyPageServiceTest {
    @Autowired
    MyPageService myPageService;

    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("유저의 프로필을 불러온다.")
    @WithUserDetails("normal@example.com")
    void getProfile() {
        //given
        Member member = memberService.getAuthenticatedMember();

        //when
        ProfileResponse profile = myPageService.getProfile(member);

        //then
        assertThat(profile.getEmail()).isEqualTo("normal@example.com");
        assertThat(profile.getNickname()).isNotNull();
        assertThat(profile.getRole()).isNotNull();
    }
}