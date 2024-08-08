package show.schedulemanagement.dto.board;

import lombok.Getter;

@Getter
public enum SearchCond {
    TITLE("제목"),CONTENTS("내용"),WRITER("닉네임");

    private final String value;

    SearchCond(String value) {
        this.value = value;
    }
}
