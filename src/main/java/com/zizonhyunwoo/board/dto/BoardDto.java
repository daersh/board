package com.zizonhyunwoo.board.dto;

import com.zizonhyunwoo.board.model.BoardEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class BoardDto {


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {
        @NotBlank
        private String title;
        @NotBlank
        private String content;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update {
        @NotNull
        private UUID id;
        @NotBlank
        private String title;
        @NotBlank
        private String content;
    }

    // 리스트 출력 용도
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response{
        UUID id;
        String title;
        LocalDateTime modifiedAt;
        String nickname;
        Integer commentCount;

        public Response(BoardEntity board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.modifiedAt = board.getModifiedAt();
            this.nickname = board.getUser().getNickname();
            this.commentCount = board.getCommentCount();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseDetail {
        UUID id;
        String title;
        String content;
        LocalDateTime modifiedAt;
        String nickname;
        List<BoardCmtDto.Response> comments;
        public ResponseDetail(BoardEntity board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.modifiedAt = board.getModifiedAt();
            this.nickname = board.getUser().getNickname();
            this.comments = board.getComments().stream().map(BoardCmtDto.Response::new).toList();
        }
    }




}
