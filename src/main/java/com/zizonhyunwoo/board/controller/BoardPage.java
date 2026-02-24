package com.zizonhyunwoo.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board")
public class BoardPage {

    @GetMapping("")
    public String BoardList() {
        return "board";
    }
    @GetMapping("write")
    public String Write() {
        return "write";
    }
    @GetMapping("detail")
    public String Detail() {
        return "detail";
    }
    @GetMapping("edit")
    public String Edit() {
        return "edit";
    }
}
