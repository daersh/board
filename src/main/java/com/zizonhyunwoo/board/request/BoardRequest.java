package com.zizonhyunwoo.board.request;

import lombok.Getter;

public class BoardRequest {

    @Getter
    public static class CreateRequest {
        String title;
        String content;
    }
}
