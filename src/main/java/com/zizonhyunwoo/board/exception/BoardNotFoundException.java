package com.zizonhyunwoo.board.exception;

public class BoardNotFoundException extends RuntimeException {
    public BoardNotFoundException(String s) {
        super(s);
    }
}
