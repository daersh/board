package com.zizonhyunwoo.board.service;

import com.zizonhyunwoo.board.dao.BoardCmtRepository;
import com.zizonhyunwoo.board.dao.BoardRepository;
import com.zizonhyunwoo.board.dao.UserRepository;
import com.zizonhyunwoo.board.exception.BoardException;
import com.zizonhyunwoo.board.model.BoardEntity;
import com.zizonhyunwoo.board.model.UserEntity;
import com.zizonhyunwoo.board.request.BoardRequest;
import com.zizonhyunwoo.board.response.BoardResponse;
import com.zizonhyunwoo.board.response.PageResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tools.jackson.databind.json.JsonMapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardService implements IBoardService {
    private final BoardRepository boardRepository;
    private final BoardCmtRepository boardCmtRepository;
    private final UserRepository userRepository;
    private final JsonMapper.Builder builder;

    public PageResponse<BoardResponse> getBoards(int page){
        Pageable pageable = PageRequest.of(page, 10);
        Page<BoardResponse> boardEntities = boardRepository.findAll(pageable).map(BoardResponse::new);
        return PageResponse.of(boardEntities);
    }

    @Transactional
    public void save(BoardRequest.Create request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated())
            throw new BoardException("존재하지 않은 사용자 ");

        String userId = authentication.getName();
        UserEntity user = userRepository.getReferenceById(UUID.fromString(userId));

        BoardEntity boardEntity = BoardEntity.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(user)
                .build();

        boardRepository.save(boardEntity);
    }

    @Override
    public BoardResponse getBoardById(String boardId) {
        return new BoardResponse(boardRepository.findById(UUID.fromString(boardId)).orElseThrow(()-> new BoardException("")));
    }

}
