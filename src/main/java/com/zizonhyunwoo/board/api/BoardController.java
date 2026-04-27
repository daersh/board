package com.zizonhyunwoo.board.api;

import com.zizonhyunwoo.board.config.UserPrincipal;
import com.zizonhyunwoo.board.dto.BoardCmtDto;
import com.zizonhyunwoo.board.dto.BoardDto;
import com.zizonhyunwoo.board.dto.PageDto;
import com.zizonhyunwoo.board.service.IBoardCmtService;
import com.zizonhyunwoo.board.service.IBoardService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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

    // 게시글 리스트 불러오기
    @GetMapping("")
    public ResponseEntity<PageDto<BoardDto.Response>> findAll(
            @RequestParam
            @Min(value = 1)
            int page
    ) {
        return ResponseEntity.ok(boardService.getBoards(page));
    }

    // 게시글 상세 조회
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardDto.ResponseDetail> findOne(
            @PathVariable
            String boardId
    ) {
        return ResponseEntity.ok(boardService.getBoardById(UUID.fromString(boardId)));
    }

    // 게시글 저장
    @PostMapping("")
    public ResponseEntity<String> save(
            @RequestBody
            @Valid
            BoardDto.Create request,
            @AuthenticationPrincipal
            UserPrincipal user
    ) {
        boardService.save(request,user);
        return ResponseEntity.ok("Saved");
    }

    // 게시글 수정
    @PutMapping("")
    public ResponseEntity<String> update(
            @RequestBody
            @Valid
            BoardDto.Update request,
            @AuthenticationPrincipal
            UserPrincipal user
    ) {

        boardService.update(request, user);
        return ResponseEntity.ok("Updated");
    }

// 게시판 삭제 
    @DeleteMapping("/{boardId}")
    public ResponseEntity<String> delete(
            @PathVariable
            UUID boardId,
            @AuthenticationPrincipal
            UserPrincipal user
    ) {
        boardService.delete(boardId, user);
        return ResponseEntity.ok("Deleted");
    }

    // 게시글 댓글 조회
    @GetMapping("/comment")
    public ResponseEntity<List<BoardCmtDto.Response>> findBoardComments(
            @RequestParam
            @Min(value = 1)
            int page,
            @RequestParam
            UUID boardId
    ) {
        return ResponseEntity.ok(boardCmtService.findBoardComments(page, boardId));
    }

    // 게시글 생성
    @PostMapping("/comment")
    public ResponseEntity<String> createBoardComment(
            @AuthenticationPrincipal
            UserPrincipal userPrincipal,
            @RequestBody
            @Valid
            BoardCmtDto.Create request
    ) {
        boardCmtService.insert(request,userPrincipal);
        return ResponseEntity.ok("Created");
    }

    @PutMapping("/comment")
    public ResponseEntity<String> updateBoardComment(
            @AuthenticationPrincipal
            UserPrincipal userPrincipal,
            @RequestBody
            @Valid
            BoardCmtDto.Update request
    ){
        boardCmtService.update(request, userPrincipal);
        return ResponseEntity.ok("Updated");
    }

}
