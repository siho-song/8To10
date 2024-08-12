package show.schedulemanagement.dto.board.reply;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import show.schedulemanagement.domain.board.reply.Reply;

@Data
@Builder
public class ReplySearchResponse {
    private Long id;
    private Long parentId;
    private String content;
    private String nickname; // 닉네임
    private String writer; // 이메일
    private long totalHearts;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ReplySearchResponse from(Reply reply){
        return ReplySearchResponse.builder()
                .id(reply.getId())
                .parentId(setParentId(reply))
                .content(reply.getContent())
                .nickname(reply.getMember().getNickname())
                .writer(reply.getMember().getEmail())
                .totalHearts(reply.getTotalHearts())
                .createdAt(reply.getCreatedAt())
                .updatedAt(reply.getUpdatedAt())
                .build();
    }

    private static Long setParentId(Reply reply) {
        if (reply.getParent() != null) {
            return reply.getParent().getId();
        }
        return null;
    }
}
