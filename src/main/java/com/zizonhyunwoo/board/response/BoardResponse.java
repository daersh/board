package com.zizonhyunwoo.board.response;

import com.zizonhyunwoo.board.dto.BoardCmtDto;
import com.zizonhyunwoo.board.model.BoardEntity;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
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
    List<BoardCmtDto.Response>  boardCmtDtos;

    public BoardResponse(BoardEntity board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.status = board.getStatus();
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
        this.nickname = board.getUser().getNickname();
        this.boardCmtDtos = board.getComments().stream().map(BoardCmtDto.Response::new).toList();
    }
}
