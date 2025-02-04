package com.eighttoten.community.dto.post;

import com.eighttoten.community.domain.post.NewPost;
import com.eighttoten.member.domain.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class PostSaveRequest {
    @NotBlank
    @Size(max = 60)
    private String title;

    @NotBlank
    @Size(min = 2)
    private String contents;

    public NewPost toNewPost(Long memberId){
        return new NewPost(title, contents, memberId);
    }
}