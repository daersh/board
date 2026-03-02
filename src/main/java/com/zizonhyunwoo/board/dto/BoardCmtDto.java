package com.zizonhyunwoo.board.dto;

import com.zizonhyunwoo.board.model.BoardCmtEntity;
import com.zizonhyunwoo.board.model.CommentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
        @NotNull
        private CommentType type;
        @NotBlank
        private String targetId;
        @NotNull
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

        public Response(BoardCmtEntity cmt) {
            this.commentId = cmt.getUuid();
            this.content = cmt.getContent();
            this.type = cmt.getType();
            this.targetId = cmt.getTargetId();
            this.nickname = cmt.getUser().getNickname();
            this.boardId = cmt.getBoard().getId();
        }

    }
}
