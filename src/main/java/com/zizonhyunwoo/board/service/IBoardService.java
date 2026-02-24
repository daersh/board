package com.zizonhyunwoo.board.service;


import com.zizonhyunwoo.board.config.UserPrincipal;
import com.zizonhyunwoo.board.request.BoardRequest;
import com.zizonhyunwoo.board.response.BoardResponse;
import com.zizonhyunwoo.board.response.PageResponse;

public interface IBoardService {
    PageResponse<BoardResponse> getBoards(int page);

    void save(BoardRequest.Create request, UserPrincipal user);

    BoardResponse getBoardById(String boardId);

    void update(BoardRequest.Update request, UserPrincipal user);
}
