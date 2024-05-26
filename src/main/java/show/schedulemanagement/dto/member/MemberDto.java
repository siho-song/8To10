package show.schedulemanagement.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import show.schedulemanagement.domain.member.Member;

@Getter
@Setter
@Builder
@ToString
public class MemberDto {
    private Long id;
    private String email;
    private String nickname;
    private String gender;
    private String mode;
    private Double score;

    public static MemberDto from(Member member){
        return MemberDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .gender(member.getGender().toString())
                .mode(member.getMode().toString())
                .score(member.getScore())
                .build();
    }
}
