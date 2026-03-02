package com.zizonhyunwoo.board.service;


import com.zizonhyunwoo.board.config.UserPrincipal;
import com.zizonhyunwoo.board.dto.BoardDto;
import com.zizonhyunwoo.board.response.BoardResponse;
import com.zizonhyunwoo.board.response.PageResponse;

import java.util.UUID;

public interface IBoardService {
    PageResponse<BoardResponse> getBoards(int page);

    void save(BoardDto.Create request, UserPrincipal user);

    BoardResponse getBoardById(String boardId);

    void update(BoardDto.Update request, UserPrincipal user);

    void delete(UUID boardId, UserPrincipal user);
}
