package com.zizonhyunwoo.board.service;

import com.zizonhyunwoo.board.config.UserPrincipal;
import com.zizonhyunwoo.board.dto.BoardCmtDto;

public interface IBoardCmtService {
    void insert(BoardCmtDto.Create create, UserPrincipal userPrincipal) ;

}
