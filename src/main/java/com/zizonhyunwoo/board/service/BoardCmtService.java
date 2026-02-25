package com.zizonhyunwoo.board.service;

import com.zizonhyunwoo.board.config.UserPrincipal;
import com.zizonhyunwoo.board.dao.BoardCmtRepository;
import com.zizonhyunwoo.board.dao.BoardRepository;
import com.zizonhyunwoo.board.dao.UserRepository;
import com.zizonhyunwoo.board.dto.BoardCmtDto;
import com.zizonhyunwoo.board.model.BoardCmtEntity;
import com.zizonhyunwoo.board.model.BoardEntity;
import com.zizonhyunwoo.board.model.UserEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardCmtService implements IBoardCmtService {
    private final BoardCmtRepository boardCmtRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public void insert(BoardCmtDto.Create create, UserPrincipal userPrincipal) {

        // 1. find board, user
        BoardEntity board = boardRepository.getReferenceById(create.getBoardId());
        UserEntity user = userRepository.getReferenceById(userPrincipal.getUserId());
        // 2. create new comment
        BoardCmtEntity boardCmt = new BoardCmtEntity();
        boardCmt.create(create,board,user);
        boardCmtRepository.save(boardCmt);
    }


}
