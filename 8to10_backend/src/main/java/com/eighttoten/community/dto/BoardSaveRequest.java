package com.eighttoten.community.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class BoardSaveRequest {
    @NotBlank
    @Size(max = 60)
    private String title;

    @NotBlank
    @Size(min = 2)
    private String contents;
}