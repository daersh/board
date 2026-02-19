package com.zizonhyunwoo.board.service;

import com.zizonhyunwoo.board.dao.BoardRepository;
import com.zizonhyunwoo.board.dao.UserRepository;
import com.zizonhyunwoo.board.exception.BoardException;
import com.zizonhyunwoo.board.model.BoardEntity;
import com.zizonhyunwoo.board.model.UserEntity;
import com.zizonhyunwoo.board.request.BoardRequest;
import com.zizonhyunwoo.board.response.BoardResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardService implements IBoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional()
    public List<BoardResponse> getBoard(int page){
        Pageable pageable = PageRequest.of(page, 10);
        return boardRepository.findAll(pageable).stream().map(BoardResponse::new).toList();
    }

    @Transactional
    public void save(BoardRequest.CreateRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated())
            throw new BoardException("존재하지 않은 사용자 ");

        String userId = authentication.getName();
        UserEntity user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new BoardException("사용자 없음"));

        BoardEntity boardEntity = BoardEntity.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(user)
                .build();

        boardRepository.save(boardEntity);
    }

}
