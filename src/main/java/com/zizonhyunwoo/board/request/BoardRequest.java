package com.zizonhyunwoo.board.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BoardRequest {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {
        private String title;
        private String content;
    }
}
