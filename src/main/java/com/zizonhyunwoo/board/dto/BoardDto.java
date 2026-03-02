package com.zizonhyunwoo.board.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

public class BoardDto {


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {
        private String title;
        private String content;
    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update {
        @NotBlank
        private UUID id;
        private String title;
        private String content;
    }
}
