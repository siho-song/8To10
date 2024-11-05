package show.schedulemanagement.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import show.schedulemanagement.domain.member.Gender;
import show.schedulemanagement.domain.member.Mode;

@Getter
@Setter
@Builder
@ToString
public class MemberDto {
    private Long id;
    private String email;
    private String nickname;
    private Gender gender;
    private Mode mode;
    private Double score;
}