package com.zizonhyunwoo.board.dto;

import com.zizonhyunwoo.board.model.BoardCmtEntity;
import com.zizonhyunwoo.board.model.CommentType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

public class BoardCmtDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Create{
        @NotBlank
        private String content;
        @NotBlank
        private CommentType type;
        @NotBlank
        private String targetId;
        @NotBlank
        private UUID boardId;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
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
