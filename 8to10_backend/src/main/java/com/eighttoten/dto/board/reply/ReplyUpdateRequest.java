package com.eighttoten.dto.board.reply;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ReplyUpdateRequest {
    @NotNull
    Long id;

    @NotBlank
    @Size(min = 2)
    String contents;
}