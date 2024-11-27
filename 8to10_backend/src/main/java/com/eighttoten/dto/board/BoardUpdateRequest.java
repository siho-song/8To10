package com.eighttoten.dto.board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class BoardUpdateRequest {
    @NotBlank
    private Long id;

    @NotBlank
    @Size(max = 60)
    private String title;

    @NotBlank
    @Size(min = 2)
    private String contents;
}