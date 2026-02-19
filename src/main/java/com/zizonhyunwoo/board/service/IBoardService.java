package com.zizonhyunwoo.board.service;


import com.zizonhyunwoo.board.request.BoardRequest;
import com.zizonhyunwoo.board.response.BoardResponse;

import java.util.List;

public interface IBoardService {
    List<BoardResponse> getBoard(int page);

    void save(BoardRequest.CreateRequest request);
}
