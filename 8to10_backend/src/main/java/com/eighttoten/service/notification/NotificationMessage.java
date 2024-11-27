package com.eighttoten.service.notification;

import lombok.Getter;

@Getter
public enum NotificationMessage {
    REPLY_ADD("회원님의 게시글에 댓글이 달렸어요 :) ") ,
    NESTED_REPLY_ADD("회원님의 댓글에 댓글이 달렸어요 :)"),
    TODO_UPDATE("잊지 않으셨죠 ? 투두 리스트 제출 마감 시간은 오후 11시 입니다 :)"),
    ;
    private final String message;

    NotificationMessage(String message) {
        this.message = message;
    }
}
