package com.zizonhyunwoo.board.service;


import com.zizonhyunwoo.board.config.UserPrincipal;
import com.zizonhyunwoo.board.dto.BoardDto;
import com.zizonhyunwoo.board.dto.PageDto;

import java.util.UUID;

public interface IBoardService {
    PageDto<BoardDto.Response> getBoards(int page);

    void save(BoardDto.Create request, UserPrincipal user);

    BoardDto.Response getBoardById(String boardId);

    void update(BoardDto.Update request, UserPrincipal user);

    void delete(UUID boardId, UserPrincipal user);
}
