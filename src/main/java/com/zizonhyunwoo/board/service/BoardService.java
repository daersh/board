package com.zizonhyunwoo.board.service;

import com.zizonhyunwoo.board.config.UserPrincipal;
import com.zizonhyunwoo.board.dao.BoardCmtRepository;
import com.zizonhyunwoo.board.dao.BoardRepository;
import com.zizonhyunwoo.board.dao.UserRepository;
import com.zizonhyunwoo.board.dto.BoardDto;
import com.zizonhyunwoo.board.exception.BoardException;
import com.zizonhyunwoo.board.model.BoardEntity;
import com.zizonhyunwoo.board.model.UserEntity;
import com.zizonhyunwoo.board.dto.PageDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardService implements IBoardService {
    private final BoardRepository boardRepository;
    private final BoardCmtRepository boardCmtRepository;
    private final UserRepository userRepository;

    public PageDto<BoardDto.Response> getBoards(int page){
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<BoardDto.Response> boardEntities = boardRepository.findAllByStatus(0,pageable).map(BoardDto.Response::new);
        return PageDto.of(boardEntities);
    }

    @Transactional
    public void save(BoardDto.Create request, UserPrincipal userPrincipal) {

        UUID userId = userPrincipal.getUserId();
        UserEntity user = userRepository.getReferenceById(userId);

        BoardEntity boardEntity = BoardEntity.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(user)
                .build();

        boardRepository.save(boardEntity);
    }

    @Override
    public BoardDto.Response getBoardById(String boardId) {
        return new BoardDto.Response(boardRepository.findById(UUID.fromString(boardId)).orElseThrow(()-> new BoardException("게시글을 찾을 수 없음")));
    }

    @Override
    @Transactional
    public void update(BoardDto.Update request, UserPrincipal principal) {
        BoardEntity board = boardRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없음"));

        if (!board.getUser().getId().equals(principal.getUserId())) {
            throw new AccessDeniedException("수정 권한이 없음");
        }

        board.update(request.getTitle(), request.getContent());
    }

    @Transactional
    @Override
    public void delete(UUID boardId, UserPrincipal userPrincipal) {
        BoardEntity board = boardRepository.findById(boardId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물"));        
        if (!board.getUser().getId().equals(userPrincipal.getUserId())) {
            throw new AccessDeniedException("삭제 권한이 없음");
        }
        board.delete();

    }

}
