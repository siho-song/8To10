package show.schedulemanagement.dto.board.reply;

import lombok.Data;

@Data
public class ReplySaveRequest {
    private Long boardId;
    private Long parentId;
    private String contents;
}
