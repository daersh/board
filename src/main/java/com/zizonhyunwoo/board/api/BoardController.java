package com.zizonhyunwoo.board.api;

import com.zizonhyunwoo.board.request.BoardRequest;
import com.zizonhyunwoo.board.response.BoardResponse;
import com.zizonhyunwoo.board.response.PageResponse;
import com.zizonhyunwoo.board.service.IBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {
    private final IBoardService boardService;

    @GetMapping("")
    public ResponseEntity<PageResponse<BoardResponse>> findAll(@RequestParam  int page) {
        return ResponseEntity.ok(boardService.getBoards(page));
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponse> findOne(@PathVariable String boardId) {
        return ResponseEntity.ok(boardService.getBoardById(boardId));
    }

    @PostMapping("")
    public ResponseEntity<String> save(@RequestBody BoardRequest.Create request) {
        boardService.save(request);
        return ResponseEntity.ok("Saved");
    }

}
