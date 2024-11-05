package show.schedulemanagement.dto.board.reply;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReplyUpdateRequest {
    @NotNull
    Long id;

    @NotBlank
    @Size(min = 2)
    String contents;
}
