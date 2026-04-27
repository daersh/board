package com.zizonhyunwoo.board.service;

import com.zizonhyunwoo.board.config.UserPrincipal;
import com.zizonhyunwoo.board.dao.BoardCmtRepository;
import com.zizonhyunwoo.board.dao.BoardRepository;
import com.zizonhyunwoo.board.dao.UserRepository;
import com.zizonhyunwoo.board.dto.BoardCmtDto;
import com.zizonhyunwoo.board.exception.BoardException;
import com.zizonhyunwoo.board.model.BoardCmtEntity;
import com.zizonhyunwoo.board.model.BoardEntity;
import com.zizonhyunwoo.board.model.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardCmtService implements IBoardCmtService {
    private final BoardCmtRepository boardCmtRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Override
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

    @Override
    public List<BoardCmtDto.Response> findBoardComments(int page, UUID boardId) {

        Pageable pageable = PageRequest.of(page-1, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        return boardCmtRepository.findAllByBoard_Id(boardId, pageable).stream()
                .map(BoardCmtDto.Response::new).toList();
    }

    @Override
    @Transactional
    public void update(BoardCmtDto.Update request, UserPrincipal userPrincipal) {
        BoardCmtEntity comment = boardCmtRepository
                .findById(request.getCommentId())
                .orElseThrow(()->new BoardException("존재하지 않는 댓글"));
        if(!comment.getUser().getId().equals(userPrincipal.getUserId()))
            throw new BoardException("댓글 수정 권한 없는 사용자");
        comment.update(request);
        log.debug("comment={}", comment);
    }


}
