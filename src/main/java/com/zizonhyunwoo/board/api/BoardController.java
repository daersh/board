package com.zizonhyunwoo.board.api;

import com.zizonhyunwoo.board.config.UserPrincipal;
import com.zizonhyunwoo.board.dto.BoardCmtDto;
import com.zizonhyunwoo.board.request.BoardRequest;
import com.zizonhyunwoo.board.response.BoardResponse;
import com.zizonhyunwoo.board.response.PageResponse;
import com.zizonhyunwoo.board.service.IBoardCmtService;
import com.zizonhyunwoo.board.service.IBoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/board") 
@RequiredArgsConstructor
public class BoardController {
    private final IBoardService boardService;
    private final IBoardCmtService boardCmtService;

    @GetMapping("")
    public ResponseEntity<PageResponse<BoardResponse>> findAll(@RequestParam  int page) {
        return ResponseEntity.ok(boardService.getBoards(page));
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponse> findOne(@PathVariable String boardId) {
        return ResponseEntity.ok(boardService.getBoardById(boardId));
    }

    @PostMapping("")
    public ResponseEntity<String> save(@RequestBody @Valid BoardRequest.Create request, @AuthenticationPrincipal UserPrincipal user) {
        boardService.save(request,user);
        return ResponseEntity.ok("Saved");
    }

    @PutMapping("")
    public ResponseEntity<String> update(
            @RequestBody @Valid BoardRequest.Update request,
            @AuthenticationPrincipal UserPrincipal user) {

        boardService.update(request, user);
        return ResponseEntity.ok("Updated");
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<String> delete(@PathVariable UUID boardId,
            @AuthenticationPrincipal UserPrincipal user) {
        boardService.delete(boardId, user);
        return ResponseEntity.ok("Deleted");
    }

    @GetMapping("/comment")
    public ResponseEntity<List<BoardCmtDto.Response>> findBoardComments(@RequestParam int page, @RequestParam String boardId) {

        return ResponseEntity.ok(boardCmtService.findBoardComments(page, UUID.fromString(boardId)));
    }

    @PostMapping("/comment")
    public ResponseEntity<String> createBoardComment(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody BoardCmtDto.Create request) {
        boardCmtService.insert(request,userPrincipal);
        return ResponseEntity.ok("Created");
    }

}
