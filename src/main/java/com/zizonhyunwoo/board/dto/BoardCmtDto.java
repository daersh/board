package com.zizonhyunwoo.board.dto;

import com.zizonhyunwoo.board.model.CommentType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

public class BoardCmtDto {

    @Getter
    @AllArgsConstructor
    public static class Create{
        private String content;
        private CommentType type;
        private String targetId;
        private UUID boardId;
    }

}
