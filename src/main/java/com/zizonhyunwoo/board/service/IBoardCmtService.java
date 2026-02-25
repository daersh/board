package com.zizonhyunwoo.board.service;

import com.zizonhyunwoo.board.config.UserPrincipal;
import com.zizonhyunwoo.board.dto.BoardCmtDto;

import java.util.List;
import java.util.UUID;

public interface IBoardCmtService {
    void insert(BoardCmtDto.Create create, UserPrincipal userPrincipal) ;

    List<BoardCmtDto.Response> findBoardComments(int page, UUID boardId);
}
