package com.zizonhyunwoo.board.api;

import com.zizonhyunwoo.board.request.BoardRequest;
import com.zizonhyunwoo.board.response.BoardResponse;
import com.zizonhyunwoo.board.service.IBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {
    private final IBoardService boardService;

    @GetMapping("")
    public ResponseEntity<List<BoardResponse>> findAll(@RequestParam  int page) {
        return ResponseEntity.ok(boardService.getBoard(page));
    }

    @PostMapping("")
    public ResponseEntity<String> save(@RequestBody BoardRequest.CreateRequest request) {
        boardService.save(request);
        return ResponseEntity.ok("Saved");
    }

}
