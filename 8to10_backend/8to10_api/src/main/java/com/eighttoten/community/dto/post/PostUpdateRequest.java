package com.eighttoten.community.dto.post;

import com.eighttoten.community.domain.post.UpdatePost;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PostUpdateRequest {
    @NotBlank
    private Long id;

    @NotBlank
    @Size(max = 60)
    private String title;

    @NotBlank
    @Size(min = 2)
    private String contents;

    public UpdatePost toUpdatePost(){
        return new UpdatePost(id, title, contents);
    }
}