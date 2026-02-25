package com.zizonhyunwoo.board.dto;

import com.zizonhyunwoo.board.model.BoardCmtEntity;
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

    @Getter
    @AllArgsConstructor
    public static class Response {
        private UUID commentId;
        private String content;
        private CommentType type;
        private String targetId;
        private String nickname;
        private UUID boardId;

        public Response(BoardCmtEntity board) {
            this.commentId = board.getUuid();
            this.content = board.getContent();
            this.type = board.getType();
            this.targetId = board.getTargetId();
            this.nickname = board.getUser().getNickname();
            this.boardId = board.getBoard().getId();
        }

    }
}
