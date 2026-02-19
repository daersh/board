package com.zizonhyunwoo.board.response;

import com.zizonhyunwoo.board.model.BoardEntity;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class BoardResponse{
    UUID id;
    String title;
    String content;
    int status;
    LocalDateTime createdAt;
    LocalDateTime modifiedAt;
    String nickname;

    public BoardResponse(BoardEntity board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.status = board.getStatus();
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
        this.nickname = board.getUser().getNickname();
    }
}
