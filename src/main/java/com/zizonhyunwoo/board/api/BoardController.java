package com.zizonhyunwoo.board.api;

import com.zizonhyunwoo.board.dao.BoardRepository;
import com.zizonhyunwoo.board.dao.UserRepository;
import com.zizonhyunwoo.board.model.BoardEntity;
import com.zizonhyunwoo.board.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @GetMapping("")
    public Page<BoardEntity> findAll(@RequestParam  int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        return boardRepository.findAll(pageable);
    }

    @PostMapping("")
    public BoardEntity save(@RequestBody BoardEntity boardEntity) {
        Optional<UserEntity> user =userRepository.findById(boardEntity.getUser().getId());
        return boardRepository.save(boardEntity);
    }

}
